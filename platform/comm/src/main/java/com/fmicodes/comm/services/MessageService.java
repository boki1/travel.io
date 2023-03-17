package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.services.util.CredentialsUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

@Service
public class MessageService {

    private static String bookingAPIHost = "booking-com.p.rapidapi.com";

    private static String rapidApiKey = CredentialsUtil.getRapidAPIKey();

    public ArrayList<Hotel> getHotelsByParams(String city, String country) {

        Integer destinationID = getDestinationIdByCityAndCountry("Istanbul", "Turkey");

//        Integer destinationID = -1746443;

        StringBuilder url = new StringBuilder("https://booking-com.p.rapidapi.com/v1/hotels/search?");
        url.append("adults_number=2");           // REQUIRED
        url.append("&dest_type=city");           // REQUIRED
        url.append("&filter_by_currency=USD");   // REQUIRED
        url.append("&checkout_date=2023-09-06"); // REQUIRED
        url.append("&checkin_date=2023-09-05");  // REQUIRED
        url.append("&order_by=popularity");      // REQUIRED
        url.append("&locale=en-gb");             // REQUIRED

        if (destinationID != null) {
            System.out.println("DESTINATION ID THAT WE END UP USING: " + destinationID);
            url.append("&dest_id=" + destinationID);
        } else {
            // TODO: Handle case when destinationID is null after API call
            url.append("&dest_id=-1746443");
        }
        url.append("&units=metric");             // REQUIRED
        url.append("&room_number=1");            // REQUIRED

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url.toString()))
                .header("X-RapidAPI-Key", rapidApiKey)
                .header("X-RapidAPI-Host", bookingAPIHost)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> hotelsResponse = null;
        try {
            hotelsResponse = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("ERROR: Making a call to the /hotels/search Booking API");
            throw new RuntimeException(e);
        }

        JSONArray hotelsArray;
        try {
            System.out.println("RESPONSE BODY: " + hotelsResponse.body());
            JSONObject responseBodyJson = new JSONObject(hotelsResponse.body());
            hotelsArray = responseBodyJson.getJSONArray("result");
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
                hotel.setReviewScore(hotelJSON.getDouble("review_score"));
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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://booking-com.p.rapidapi.com/v1/hotels/locations?name=" + cityName + "&locale=en-gb"))
                .header("X-RapidAPI-Key", rapidApiKey)
                .header("X-RapidAPI-Host", "booking-com.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> destinationsResponse = null;
        try {
            destinationsResponse = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            System.out.println("ERROR: Making a call to the /hotels/locations Booking API");
            throw new RuntimeException(e);
        }

        JSONArray destinationsArray = null;
        try {
            destinationsArray = new JSONArray(destinationsResponse.body());
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

}
