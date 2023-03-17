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

@Service
public class MessageService {

    private static String bookingAPIHost = "booking-com.p.rapidapi.com";

    private static String rapidApiKey = CredentialsUtil.getRapidAPIKey();

    public String getHotelsByParams() {

        Integer destinationID = getDestinationIdByCityAndCountry("Berlin", "Germany");

        StringBuilder url = new StringBuilder("https://booking-com.p.rapidapi.com/v1/hotels/search?");
        url.append("adults_number=2");           // REQUIRED
        url.append("&dest_type=city");           // REQUIRED
        url.append("&filter_by_currency=USD");   // REQUIRED
        url.append("&checkout_date=2023-09-06"); // REQUIRED
        url.append("&checkin_date=2023-09-05");  // REQUIRED
        url.append("&order_by=popularity");      // REQUIRED
        url.append("&locale=en-gb");             // REQUIRED
        url.append("&dest_id=-553173");          // REQUIRED - is problematic. You can get this from GET Search locations
        url.append("&units=metric");             // REQUIRED
        url.append("&room_number=1");            // REQUIRED
//        url.append("&categories_filter_ids=class%3A%3A2%2Cclass%3A%3A4%2Cfree_cancellation%3A%3A1");
//        url.append("&children_number=2");
//        url.append("&children_ages=5%2C0");
//        url.append("&page_number=0");
//        url.append("&include_adjacency=true");


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url.toString()))
                .header("X-RapidAPI-Key", rapidApiKey)
                .header("X-RapidAPI-Host", bookingAPIHost)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        JSONArray hotels;
        try {
            JSONObject responseBodyJson = new JSONObject(response.body());
            hotels = responseBodyJson.getJSONArray("result");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < hotels.length(); i++) {
            try {
                JSONObject JSONhotel = hotels.getJSONObject(i);

                Hotel hotel = new Hotel();
                hotel.setHotelId(JSONhotel.getInt("hotel_id"));
                hotel.setHotelName(JSONhotel.getString("hotel_name_trans"));

//                System.out.println(hotel);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }


        return "Hello World";
    }


    private Integer getDestinationIdByCityAndCountry(String cityName, String countryName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://booking-com.p.rapidapi.com/v1/hotels/locations?name=" + cityName + "&locale=en-gb"))
                .header("X-RapidAPI-Key", rapidApiKey)
                .header("X-RapidAPI-Host", "booking-com.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        JSONArray destinations = null;
        try {
            destinations = new JSONArray(response.body());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Integer destinationId = null;

        for (int i = 0; i < destinations.length(); i++) {
            try {
                JSONObject destination = destinations.getJSONObject(i);
                System.out.println("DESTINATION: " + destination);
                System.out.println(destination.getString("label") + ": " + destination.getInt("dest_id"));

                if (destination.get("country").equals(countryName)) {
                    destinationId = destination.getInt("dest_id");
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(response.body());

        return destinationId;
    }

}
