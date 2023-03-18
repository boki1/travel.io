package com.fmicodes.comm.services;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class AnalyzerService {

    @Value("${analyzer.api.host}")
    private String analyzerHost;


    public String analyzeMessage(String message) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String analyzedMessageResponse = null;
        try {
            Response response = client.prepare("POST",  analyzerHost + "api/v1/analyzer")
                    .setBody(message)
                    .execute()
                    .get();

            analyzedMessageResponse = response.getResponseBody();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        JSONObject analyzedMessageJSON;
        try {
            analyzedMessageJSON = new JSONObject(analyzedMessageResponse);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONArray locationsJSONArray;
        try {
            locationsJSONArray = analyzedMessageJSON.getJSONArray("locations");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < locationsJSONArray.length(); i++) {

            JSONArray locationJSON;
            try {
                locationJSON = locationsJSONArray.getJSONArray(i);
                String city = locationJSON.getString(1);

                System.out.println("CITY: " + city);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }


        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return analyzedMessageResponse;
    }
}
