package com.fmicodes.comm.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmicodes.comm.DTO.Location;
import com.fmicodes.comm.DTO.mapsAPI.Landmark;
import com.fmicodes.comm.DTO.mapsAPI.RestaurantInfo;
import com.fmicodes.comm.services.util.CredentialsUtil;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class GoogleMapsService {
    private static final String mapsApiKey = CredentialsUtil.getMapsAPIKey();
    private final OkHttpClient client = new OkHttpClient();
    private final GeoApiContext context;

    public GoogleMapsService() {
        context = new GeoApiContext.Builder().apiKey(mapsApiKey).build();
    }

    public List<Landmark> searchLandmarks(List<String> landmarksList, Location location) {
        List<Landmark> landmarks = new ArrayList<>();

        try {
            for (String landmarkLiteral : landmarksList) {
                PlacesSearchResponse response = PlacesApi.textSearchQuery(context,
                        landmarkLiteral + " in " + location.getCity() + " " + location.getCountry()).await();

                PlacesSearchResult result = response.results[0];
                PlaceDetails details = PlacesApi.placeDetails(context, result.placeId).await();
                Landmark landmark = new Landmark(
                        details.name,
                        details.formattedAddress,
                        details.geometry.location.lat,
                        details.geometry.location.lng,
                        details.openingHours != null ? Arrays.toString(details.openingHours.weekdayText) : null,
                        details.openingHours != null ? details.rating : null,
                        details.openingHours != null ? details.openingHours.openNow : null
                );
                landmarks.add(landmark);
            }
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException("Failed fetching landmarks with exception: " + e.getMessage());
        }

        return landmarks;
    }

    public String getPhotoUrl(String photoReference, int maxHeight, int maxWidth) {
        return "https://maps.googleapis.com/maps/api/place/photo?maxheight=" + maxHeight + "&maxwidth=" + maxWidth
                + "&photoreference=" + photoReference + "&key=" + mapsApiKey;
    }


    public ArrayList<RestaurantInfo> getNearbyRestaurants(double latitude, double longitude, int radius) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + latitude + "," + longitude + "&radius=" + radius + "&type=restaurant&key=" + mapsApiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        ArrayList<RestaurantInfo> restaurants = new ArrayList<>();

        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = Objects.requireNonNull(response.body()).string();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);


            if (jsonNode.has("results")) {
                for (JsonNode result : jsonNode.get("results")) {

                    if (restaurants.size() > 3) {
                        break;
                    }

                    RestaurantInfo restaurant = new RestaurantInfo();

                    if (result.has("name")) {
                        restaurant.setName(result.get("name").asText());
                    }

                    if (result.has("vicinity")) {
                        restaurant.setLocation(result.get("vicinity").asText());
                    }

                    if (result.has("rating") && result.get("rating") != null) {
                        restaurant.setRating(result.get("rating").asDouble());
                    }

                    if (result.has("price_level")) {
                        restaurant.setPriceLevel(result.get("price_level").asInt());
                    }

                    if (result.has("user_ratings_total")) {
                        restaurant.setUserRatingsTotal(result.get("user_ratings_total").asInt());
                    }

                    if (result.has("opening_hours") && result.get("opening_hours").has("open_now")) {
                        boolean isOpen = result.get("opening_hours").get("open_now").asBoolean();
                        restaurant.setOpeningHours(isOpen ? "Open now" : "Closed");
                    }

                    if (result.has("place_id")) {
                        String placeId = result.get("place_id").asText();
                        restaurant.setGoogleMapsLink("https://www.google.com/maps/place/?q=place_id:" + placeId);
                    }


                    if (result.has("photos")) {
                        JsonNode photo = result.get("photos").get(0);
                        int photoWidth = 1000;
                        int photoHeight = 1000;
                        if (photo.has("width")) {
                            photoWidth = photo.get("width").asInt();
                        }
                        if (photo.has("height")) {
                            photoHeight = photo.get("height").asInt();
                        }

                        if (photo.has("photo_reference")) {
                            String photoReference = photo.get("photo_reference").asText();
                            restaurant.setPhotoUrl(getPhotoUrl(photoReference, photoHeight, photoWidth));
                        }
                    }

                    restaurants.add(restaurant);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("ERROR - Getting nearby restaurants from Google Maps API: " + e.getMessage());
        }

        return restaurants;
    }
}
