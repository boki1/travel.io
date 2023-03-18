package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.Location;
import com.fmicodes.comm.exceptions.DeserializingJSONException;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.asynchttpclient.request.body.generator.BodyGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@Service
public class AnalyzerService {

    @Value("${analyzer.host}")
    private String analyzerHost;


    public String analyzeMessage(String message) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String analyzedMessageResponse = null;
        try {
            Response response = client.prepare("POST", "http://" + analyzerHost + "/api/v1/analyzer")
                    .setBody(message)
                    .execute()
                    .get();

            analyzedMessageResponse = response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERROR - Communicating with Flask API: " + e.getMessage());
        }

        JSONObject analyzedMessageJSON;
        try {
            analyzedMessageJSON = new JSONObject(analyzedMessageResponse);
        } catch (JSONException e) {
            throw new DeserializingJSONException("ERROR - Deserializing analyzedMessageResponse " + e.getMessage());
        }

        JSONArray locationsJSONArray;
        try {
            locationsJSONArray = analyzedMessageJSON.getJSONArray("locations");

            for (int i = 0; i < locationsJSONArray.length(); i++) {

                JSONArray locationJSON;
                locationJSON = locationsJSONArray.getJSONArray(i);
                String city = locationJSON.getString(1);

                System.out.println("CITY: " + city);
            }

        } catch (JSONException e) {
            throw new DeserializingJSONException("ERROR - Deserializing locationsJSONArray: " + e.getMessage());
        }

        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException("ERROR - Closing AsyncHttpClient: " + e.getMessage());
        }

        return analyzedMessageResponse;
    }

    public String getAirportIATACodeByLocation(Location location) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String airportIATACode = "";
        try {
            Response response = client.prepare("POST", "http://" + analyzerHost + "/api/v1/airports")
                    .setHeader("Content-Type", "application/json")
                    .setBody("{\"city\": \"" + location.getCity() + "\", \"country\": \"" + location.getCountry() + "\"}")
                    .execute()
                    .get();

            airportIATACode = response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERROR - Communicating with Flask API: " + e.getMessage());
        }

        return airportIATACode;
    }
}
