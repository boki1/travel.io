package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.Location;
import com.fmicodes.comm.DTO.OpenAIDestinationResponse;
import com.fmicodes.comm.exceptions.DeserializingJSONException;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



@Service
public class AnalyzerService {

    @Value("${analyzer.host}")
    private String analyzerHost;


    public ArrayList<OpenAIDestinationResponse> analyzeMessage(String message) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String analyzedMessageResponse;
        try {
            Response response = client.prepare("POST", "http://" + analyzerHost + "/api/v1/analyzer")
                    .setBody(message)
                    .execute()
                    .get();

            analyzedMessageResponse = response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERROR - Communicating with Flask API: " + e.getMessage());
        }

        JSONArray analyzedMessageJSON;
        try {
            analyzedMessageJSON = new JSONArray(analyzedMessageResponse);
        } catch (JSONException e) {
            throw new DeserializingJSONException("ERROR - Deserializing analyzedMessageResponse " + e.getMessage());
        }

        ArrayList<OpenAIDestinationResponse> openAIDestinationResponses = new ArrayList<>();
        JSONObject destinationsJSONObject;
        try {
            destinationsJSONObject = analyzedMessageJSON.getJSONObject(0);
            JSONArray destinationsJSONArray = destinationsJSONObject.getJSONArray("destinations");

            for (int i = 0; i < destinationsJSONArray.length(); i++) {

                JSONObject destinationJSONObject = destinationsJSONArray.getJSONObject(i);

                JSONObject locationJSON = destinationJSONObject.getJSONObject("location");
                String city = locationJSON.getString("city");
                String country = locationJSON.getString("country");
                String description = locationJSON.getString("description");

                Location location = new Location(city, country, description);

                ArrayList<String> landmarksArrayList = new ArrayList<>();
                JSONArray landmarks = destinationJSONObject.getJSONArray("landmarks");
                for (int j = 0; j < landmarks.length(); j++) {
                    landmarksArrayList.add(landmarks.getString(j));
                }

                ArrayList<String> activitiesArrayList = new ArrayList<>();
                JSONArray activities = destinationJSONObject.getJSONArray("activities");
                for (int j = 0; j < activities.length(); j++) {
                    activitiesArrayList.add(activities.getString(j));
                }

                OpenAIDestinationResponse openAIDestinationResponse = new OpenAIDestinationResponse();
                openAIDestinationResponse.setLocation(location);
                openAIDestinationResponse.setLandmarks(landmarksArrayList);
                openAIDestinationResponse.setActivities(activitiesArrayList);


                openAIDestinationResponses.add(openAIDestinationResponse);
            }

        } catch (JSONException e) {
            throw new DeserializingJSONException("ERROR - Deserializing destinationsJSONArray: " + e.getMessage());
        }

        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException("ERROR - Closing AsyncHttpClient: " + e.getMessage());
        }

        return openAIDestinationResponses;
    }

    public String getAirportIATACodeByLocation(Location location) {
        String airportIATACode;

        AsyncHttpClient client = new DefaultAsyncHttpClient();
        try {
            Response response = client.prepare("POST", "http://" + analyzerHost + "/api/v1/airports")
                    .setHeader("Content-Type", "application/json")
                    .setBody("{\"city\": \"" + location.getCity() + "\", \"country\": \"" + location.getCountry() + "\"}")
                    .execute()
                    .get();

            JSONObject airportData = new JSONObject(response.getResponseBody());
            airportIATACode = airportData.getString("iata");
        } catch (InterruptedException | ExecutionException | JSONException e) {
            throw new RuntimeException("ERROR - Communicating with airports API: " + e.getMessage());
        }

        return airportIATACode;
    }
}
