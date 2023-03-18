package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.Location;
import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.exceptions.DeserializingJSONException;
import com.fmicodes.comm.services.util.CredentialsUtil;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final String rapidApiKey = CredentialsUtil.getRapidAPIKey();
    private static final Integer MAX_HOTELS_SUGGESTION = 2;
    private static final Double HOTEL_PRICE_BUFFER = 0.15;
    @Value("${booking.host}")
    private String bookingAPIHost;

    public ArrayList<Hotel> getHotelsByParams(Location location, String checkInDate, String checkOutDate, Double maximumBudget, String airportCode) {
        StringBuilder url = new StringBuilder("https://" + bookingAPIHost + "/v1/hotels/search?");
        url.append("adults_number=2");
        url.append("&dest_type=city");
        url.append("&filter_by_currency=USD");
        url.append("&checkout_date=").append(checkOutDate);
        url.append("&checkin_date=").append(checkInDate);
        url.append("&order_by=popularity");
        url.append("&locale=en-gb");
        url.append("&units=metric");
        url.append("&room_number=1");

        Integer destinationID = getDestinationIdByCityAndCountry(location);
        if (destinationID != null) {
            System.out.println("DESTINATION ID THAT WE END UP USING: " + destinationID);
            url.append("&dest_id=").append(destinationID);
        } else {
            // TODO: Handle case when destinationID is null after API call
            url.append("&dest_id=" + "-1746443"); // stupid error handling for now

//            throw new DestinationNotFoundException("Destination not found");
        }

        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String hotelsResponse;
        try {
            Response response = client.prepare("GET", url.toString())
                    .setHeader("X-RapidAPI-Key", rapidApiKey)
                    .setHeader("X-RapidAPI-Host", bookingAPIHost)
                    .execute()
                    .get();

            hotelsResponse = response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERROR: Calling the get hotels API: " + e.getMessage());
        }

        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException("ERROR: Closing client: " + e.getMessage());
        }

        JSONArray hotelsArray = new JSONArray();
        try {
            JSONObject responseBodyJson = new JSONObject(hotelsResponse);
            if (responseBodyJson.has("result")) {
                hotelsArray = responseBodyJson.getJSONArray("result");
            }
        } catch (JSONException e) {
            throw new DeserializingJSONException("ERROR: Parsing hotels response body to JSON: " + e.getMessage());
        }

        ArrayList<Hotel> hotelSuggestions = new ArrayList<>();

        for (int i = 0; i < hotelsArray.length(); i++) {
            try {
                JSONObject hotelJSON = hotelsArray.getJSONObject(i);

                Hotel hotel = new Hotel();
                hotel.setHotelId(hotelJSON.getInt("hotel_id"));
                hotel.setHotelName(hotelJSON.getString("hotel_name_trans"));
                hotel.setUrl(hotelJSON.getString("url"));
                hotel.setAddress(hotelJSON.getString("address"));
                hotel.setLatitude(hotelJSON.getDouble("latitude"));
                hotel.setLongitude(hotelJSON.getDouble("longitude"));
                hotel.setPrice(hotelJSON.getDouble("min_total_price"));
                hotel.setCurrency(hotelJSON.getString("currencycode"));
                hotel.setAirportCode(airportCode);

                if (hotelJSON.has("review_score") && !hotelJSON.isNull("review_score")) {
                    hotel.setReviewScore(hotelJSON.getDouble("review_score"));
                }

                hotel.setMaxPhotoUrl(hotelJSON.getString("max_photo_url"));

                hotelSuggestions.add(hotel);
            } catch (JSONException e) {
                throw new DeserializingJSONException("ERROR: Parsing hotel JSON object: " + e.getMessage());
            }
        }


        hotelSuggestions = accountForMaximumBudget(hotelSuggestions, maximumBudget);

        hotelSuggestions = filterHotels(hotelSuggestions);

        return hotelSuggestions;
    }

    public ArrayList<Hotel> filterHotels(ArrayList<Hotel> hotels) {
        hotels = hotels
                .stream()
                .filter(hotel -> hotel.getAirportCode() != null)
                .collect(Collectors.toCollection(ArrayList::new));
        hotels = hotels
                .stream()
                .filter(hotel -> hotel.getReviewScore() != null)
                .sorted(Comparator.comparing(Hotel::getReviewScore).reversed())
                .collect(Collectors.toCollection(ArrayList::new));

        hotels = hotels
                .stream()
                .limit(MAX_HOTELS_SUGGESTION)
                .collect(Collectors.toCollection(ArrayList::new));

        return hotels;
    }

    private Integer getDestinationIdByCityAndCountry(Location location) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String destinationsResponse;
        try {
            Response response = client.prepare("GET", "https://" + bookingAPIHost + "/v1/hotels/locations?name=" + location.getCity() + "&locale=en-gb")
                    .setHeader("X-RapidAPI-Key", rapidApiKey)
                    .setHeader("X-RapidAPI-Host", bookingAPIHost)
                    .execute()
                    .get();

            destinationsResponse = response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERROR - Fetching destinations from /v1/hotels/locations: " + e.getMessage());
        }

        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException("ERROR - Closing AsyncHttpClient: " + e.getMessage());
        }

        JSONArray destinationsArray;
        try {
            destinationsArray = new JSONArray(destinationsResponse);
        } catch (JSONException e) {
            throw new DeserializingJSONException("ERROR: Parsing destinations response body to JSON: " + e.getMessage());
        }

        Integer destinationId = null;
        for (int i = 0; i < destinationsArray.length(); i++) {
            try {
                JSONObject destination = destinationsArray.getJSONObject(i);

                if (destination.get("country").equals(location.getCountry()) && destination.getInt("dest_id") < 0) {
                    destinationId = destination.getInt("dest_id");
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return destinationId;
    }

    private ArrayList<Hotel> accountForMaximumBudget(ArrayList<Hotel> hotels, Double maximumBudget) {
        Double finalMaximumBudget = maximumBudget + maximumBudget * HOTEL_PRICE_BUFFER;

        return hotels
                .stream()
                .filter(hotel -> hotel.getPrice() <= finalMaximumBudget)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
