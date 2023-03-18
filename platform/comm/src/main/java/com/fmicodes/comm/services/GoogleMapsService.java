package com.fmicodes.comm.services;

import com.fmicodes.comm.services.util.CredentialsUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GoogleMapsService {
    private static final String mapsApiKey = CredentialsUtil.getMapsAPIKey();
    private final OkHttpClient client = new OkHttpClient();

    public String getHotelCoordinates(String hotelName) throws IOException {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
                + URLEncoder.encode(hotelName, StandardCharsets.UTF_8) + "&key=" + mapsApiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
//            TODO: may return null
            return response.body().string();
        }
    }

    public String getNearbyRestaurants(double latitude, double longitude, int radius) throws IOException {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + latitude + "," + longitude + "&radius=" + radius + "&type=restaurant&key=" + mapsApiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
//            TODO: may return null
            return response.body().string();
        }
    }
}
