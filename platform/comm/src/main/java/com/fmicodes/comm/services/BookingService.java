package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.exceptions.DestinationNotFoundException;
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
import java.util.concurrent.ExecutionException;

@Service
public class BookingService {

    @Value("${booking.host}")
    private String bookingAPIHost;

    private static String rapidApiKey = CredentialsUtil.getRapidAPIKey();

    private static final Integer MAX_HOTELS_SUGGESTION = 2;
    private static final Double HOTEL_PRICE_BUFFER = 0.15;

    public ArrayList<Hotel> getHotelsByParams(String city, String country, String checkInDate, String checkOutDate, Double maximumBudget) {
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

        Integer destinationID = getDestinationIdByCityAndCountry(city, country);
        if (destinationID != null) {
            System.out.println("DESTINATION ID THAT WE END UP USING: " + destinationID);
            url.append("&dest_id=" + destinationID);
        } else {
            // TODO: Handle case when destinationID is null after API call

            throw new DestinationNotFoundException("Destination not found");
        }

        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String hotelsResponse = null;
        try {
            Response response = client.prepare("GET", url.toString())
                    .setHeader("X-RapidAPI-Key", rapidApiKey)
                    .setHeader("X-RapidAPI-Host", bookingAPIHost)
                    .execute()
                    .get();

            hotelsResponse = response.getResponseBody();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONArray hotelsArray = new JSONArray();
        try {
            JSONObject responseBodyJson = new JSONObject(hotelsResponse);
            if (responseBodyJson.has("result")) {
                hotelsArray = responseBodyJson.getJSONArray("result");
            }
        } catch (JSONException e) {
            System.out.println("ERROR: Parsing hotels response body to JSON");
            throw new RuntimeException(e);
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

                if (hotelJSON.has("review_score") && !hotelJSON.isNull("review_score")) {
                    hotel.setReviewScore(hotelJSON.getDouble("review_score"));
                }
                hotel.setMaxPhotoUrl(hotelJSON.getString("max_photo_url"));

                hotelSuggestions.add(hotel);
            } catch (JSONException e) {
                System.out.println("ERROR: Parsing hotel JSON object");
                throw new RuntimeException(e);
            }
        }

        hotelSuggestions = accountForMaximumBudget(hotelSuggestions, maximumBudget);

        return hotelSuggestions;
    }


    private Integer getDestinationIdByCityAndCountry(String cityName, String countryName) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String destinationsResponse = null;
        try {
            Response response = client.prepare("GET", "https://" + bookingAPIHost + "/v1/hotels/locations?name=" + cityName + "&locale=en-gb")
                    .setHeader("X-RapidAPI-Key", rapidApiKey)
                    .setHeader("X-RapidAPI-Host", bookingAPIHost)
                    .execute()
                    .get();

            destinationsResponse = response.getResponseBody();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONArray destinationsArray = null;
        try {
            destinationsArray = new JSONArray(destinationsResponse);
        } catch (JSONException e) {
            System.out.println("ERROR: Parsing destinations response body to JSON");
            throw new RuntimeException(e);
        }

        Integer destinationId = null;
        for (int i = 0; i < destinationsArray.length(); i++) {
            try {
                JSONObject destination = destinationsArray.getJSONObject(i);

                if (destination.get("country").equals(countryName) && destination.getInt("dest_id") < 0) {
                    destinationId = destination.getInt("dest_id");
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return destinationId;
    }

    private ArrayList<Hotel> accountForMaximumBudget(ArrayList<Hotel> hotels, Double maximumBudget) {
        maximumBudget += maximumBudget * HOTEL_PRICE_BUFFER; // Add a 15% buffer to the maximum budget.

        ArrayList<Hotel> hotelsWithinBudget = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.getPrice() <= maximumBudget) {
                hotelsWithinBudget.add(hotel);
            }

            if (hotelsWithinBudget.size() >= MAX_HOTELS_SUGGESTION) {
                break;
            }
        }

        return hotelsWithinBudget;
    }

    public ArrayList<Hotel> checkAirportsCompatibility(ArrayList<Hotel> hotels) throws IOException, ExecutionException, InterruptedException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        for (Hotel hotel : hotels) {
           Response response = client.prepare("GET", "https://" + bookingAPIHost + "/v1/hotels/nearby-places?locale=en-gb&hotel_id=" + hotel.getHotelId())
                    .setHeader("X-RapidAPI-Key", rapidApiKey)
                    .setHeader("X-RapidAPI-Host", bookingAPIHost)
                    .execute()
                    .get();

            try {
                JSONObject responseBodyJson = new JSONObject(response.getResponseBody());
                JSONObject transport = responseBodyJson.getJSONObject("transport");
                if (transport.has("airport")) {
                    JSONObject airport = transport.getJSONObject("airport");
                    hotel.setAirportCode(airport.getString("code"));
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

        client.close();


        return hotels;
    }

}
