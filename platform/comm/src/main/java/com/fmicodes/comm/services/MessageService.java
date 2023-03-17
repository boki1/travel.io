package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.services.util.CredentialsUtil;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Destination IDs:
 * -2601889: London
 * -1746443: Berlin
 * -755070: Istanbul
 */

@Service
public class MessageService {

    @Autowired
    private AnalyzerService analyzerService;

    private static String bookingAPIHost = "booking-com.p.rapidapi.com";

    private static String rapidApiKey = CredentialsUtil.getRapidAPIKey();

    public String getMessageAnalysis(String message) {
        String analyzerResponse = analyzerService.analyzeMessage(message);
        return analyzerResponse;
    }

    public ArrayList<Hotel> getHotelsByParams(String city, String country) {
        StringBuilder url = new StringBuilder("https://booking-com.p.rapidapi.com/v1/hotels/search?");
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

        int amountOfHotels = hotelsArray.length() <= 2 ? hotelsArray.length() : 2;
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
            Response response = client.prepare("GET", "https://booking-com.p.rapidapi.com/v1/hotels/locations?name=" + cityName + "&locale=en-gb")
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

}
