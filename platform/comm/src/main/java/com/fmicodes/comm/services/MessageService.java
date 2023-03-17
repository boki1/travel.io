package com.fmicodes.comm.services;

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

    private static String rapidAPIKey = "";
    private static String bookingAPIHost = "booking-com.p.rapidapi.com";

    public String getHotelsByParams() {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://booking-com.p.rapidapi.com/v1/hotels/search?adults_number=2&dest_type=city&filter_by_currency=AED&checkout_date=2023-09-06&checkin_date=2023-09-05&order_by=popularity&locale=en-gb&dest_id=-553173&units=metric&room_number=1&categories_filter_ids=class%3A%3A2%2Cclass%3A%3A4%2Cfree_cancellation%3A%3A1&children_number=2&children_ages=5%2C0&page_number=0&include_adjacency=true"))
                .header("X-RapidAPI-Key", rapidAPIKey)
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

        JSONObject responseBodyJson;
        JSONArray hotels;
        try {
            responseBodyJson = new JSONObject(response.body());
            hotels = responseBodyJson.getJSONArray("result");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < hotels.length(); i++) {
            try {
                JSONObject hotel = hotels.getJSONObject(i);
                System.out.println(hotel.get("hotel_name_trans"));

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }


        return "Hello World";
    }

}
