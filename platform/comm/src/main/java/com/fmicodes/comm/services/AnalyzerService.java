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
        String analyzedMessageResponse = "[\n" +
                "    {\n" +
                "        \"destinations\": [\n" +
                "            {\n" +
                "                \"activities\": [\n" +
                "                    \"Surfing\"\n" +
                "                ],\n" +
                "                \"landmarks\": [\n" +
                "                    \"Grand Plage\",\n" +
                "                    \"Rocher de la Vierge\",\n" +
                "                    \"Miramar Palace\"\n" +
                "                ],\n" +
                "                \"location\": {\n" +
                "                    \"city\": \"Biarritz\",\n" +
                "                    \"country\": \"France\",\n" +
                "                    \"description\": \"Grand Plage is a popular beach with golden sand and clear water, ideal for sunbathing, swimming, and surfing. \\n    Rocher de la Vierge is a rocky promontory with a statue of the Virgin Mary overlooking the sea, offering great views and photo opportunities.\\n    Miramar Palace is a beautiful palace built in the 19th century for Empress Eugenie, now converted into a hotel and spa.\\n    Surfing is a popular activity in Biarritz, with many surf schools and rental shops available.\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"activities\": [\n" +
                "                    \"Sailing\"\n" +
                "                ],\n" +
                "                \"landmarks\": [\n" +
                "                    \"La Concurrence Beach\",\n" +
                "                    \"Old Port\",\n" +
                "                    \"Maritime Museum\"\n" +
                "                ],\n" +
                "                \"location\": {\n" +
                "                    \"city\": \"La Rochelle\",\n" +
                "                    \"country\": \"France\",\n" +
                "                    \"description\": \"La Concurrence Beach is a sandy beach near the city center, with many restaurants and shops nearby.\\n    Old Port is a picturesque harbor with colorful boats and lively atmosphere, surrounded by historic buildings and cafes.\\n    Maritime Museum is a museum dedicated to the maritime history of La Rochelle, with many exhibits and interactive displays.\\n    Sailing is a popular activity in La Rochelle, with many boat rental and sailing schools available.\"\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"activities\": [\n" +
                "                    \"Kite surfing\"\n" +
                "                ],\n" +
                "                \"landmarks\": [\n" +
                "                    \"Dune du Pilat\",\n" +
                "                    \"Arcachon Bay\",\n" +
                "                    \"Cap Ferret\"\n" +
                "                ],\n" +
                "                \"location\": {\n" +
                "                    \"city\": \"Arcachon\",\n" +
                "                    \"country\": \"France\",\n" +
                "                    \"description\": \"Dune du Pilat is a large sand dune overlooking the ocean, offering breathtaking views and a challenging climb.\\n    Arcachon Bay is a shallow lagoon with many oyster farms and fishing villages, offering boat tours and seafood restaurants.\\n    Cap Ferret is a peninsula with many beaches and pine forests, accessible by ferry or car.\\n    Kite surfing is a popular activity in Arcachon, with many kite schools and rental shops available.\"\n" +
                "                }\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "]";

//        try {
//            Response response = client.prepare("POST", "http://" + analyzerHost + "/api/v1/analyzer")
//                    .setBody(message)
//                    .execute()
//                    .get();
//
//            analyzedMessageResponse = response.getResponseBody();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException("ERROR - Communicating with Flask API: " + e.getMessage());
//        }

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

                System.out.println("CITY: " + city);
                System.out.println("COUNTRY: " + country);

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
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String airportIATACode;
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
