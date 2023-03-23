package com.fmicodes.comm.services;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UnsplashService {

    public String getUnsplashImage(String location) {
        String destinationsResponse;
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        try {
            Response response = client.prepare("GET", "https://api.unsplash.com/search/photos?page=1&query=" + location)
                    .setHeader("Authorization", "Client-ID " + "7394sfnNucC9j1r5olgGsJ2-hiE6_28Xdi7R1KBC1rE")
                    .execute()
                    .get();

            destinationsResponse = response.getResponseBody();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("ERROR - Fetching destinations from /v1/hotels/locations: " + e.getMessage());
        }


        String normalSizeImage = "";
        try {
            JSONObject jsonResponse = new JSONObject(destinationsResponse);
            JSONArray results = jsonResponse.getJSONArray("results");

            if (results.length() != 0) {
                JSONObject firstResult = results.getJSONObject(0);
                JSONObject urls = firstResult.getJSONObject("urls");
                normalSizeImage = urls.getString("regular");
                System.out.println("JSON RESPONSE: " + urls);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return normalSizeImage;
    }

}
