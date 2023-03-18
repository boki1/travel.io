package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.booking.Hotel;
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

    public ArrayList<Hotel> getHotelsByParams(String city, String country) {
        StringBuilder url = new StringBuilder("https://" + bookingAPIHost + "/v1/hotels/search?");
        url.append("adults_number=2");           // REQUIRED
        url.append("&dest_type=city");           // REQUIRED
        url.append("&filter_by_currency=USD");   // REQUIRED
        url.append("&checkout_date=2023-09-06"); // REQUIRED
        url.append("&checkin_date=2023-09-05");  // REQUIRED
        url.append("&order_by=popularity");      // REQUIRED
        url.append("&locale=en-gb");             // REQUIRED
        url.append("&units=metric");             // REQUIRED
        url.append("&room_number=1");            // REQUIRED

        Integer destinationID = getDestinationIdByCityAndCountry(city, country);
        if (destinationID != null) {
            System.out.println("DESTINATION ID THAT WE END UP USING: " + destinationID);
            url.append("&dest_id=" + destinationID);
        } else {
            // TODO: Handle case when destinationID is null after API call
            url.append("&dest_id=-1746443");
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

        JSONArray hotelsArray;
        try {
            System.out.println("RESPONSE BODY: " + hotelsResponse);
            JSONObject responseBodyJson = new JSONObject(hotelsResponse);
            hotelsArray = responseBodyJson.getJSONArray("result");
        } catch (JSONException e) {
            System.out.println("ERROR: Parsing hotels response body to JSON");
            throw new RuntimeException(e);
        }

        ArrayList<Hotel> hotelSuggestions = new ArrayList<>();

        int amountOfHotels = hotelsArray.length() <= MAX_HOTELS_SUGGESTION ? hotelsArray.length() : MAX_HOTELS_SUGGESTION;
        for (int i = 0; i < amountOfHotels; i++) {
            try {
                JSONObject hotelJSON = hotelsArray.getJSONObject(i);

                Hotel hotel = new Hotel();
                hotel.setHotelId(hotelJSON.getInt("hotel_id"));
                hotel.setHotelName(hotelJSON.getString("hotel_name_trans"));
                hotel.setUrl(hotelJSON.getString("url"));
                hotel.setAddress(hotelJSON.getString("address"));

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
