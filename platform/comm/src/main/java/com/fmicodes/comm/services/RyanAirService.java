package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.travel.Flight;
import com.fmicodes.comm.services.util.CredentialsUtil;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Service
public class RyanAirService {

    @Value("${ryanair.host}")
    private String ryanAirHost;

    private static String rapidApiKey = CredentialsUtil.getRapidAPIKey();

    public ArrayList<Flight> getFlightsBetweenTwoAirports(String locationAirportCode, String destinationAirportCode, String originDepartureDate) {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        JSONObject routesJson = null;
        JSONArray originToDestinationRoutes = null;
        try {
            Response response = client.prepare("GET", "https://ryanair.p.rapidapi.com/flights?origin_code=LGW&destination_code=DUB&origin_departure_date=2023-09-28&destination_departure_date=2023-10-28")
                    .setHeader("X-RapidAPI-Key", rapidApiKey)
                    .setHeader("X-RapidAPI-Host", ryanAirHost)
                    .execute()
                    .get();

            routesJson = new JSONObject(response.getResponseBody());
            originToDestinationRoutes = routesJson.getJSONArray("origin_to_destination_trip");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Flight> availableFlights = new ArrayList<>();
        for (int i = 0; i < originToDestinationRoutes.length(); i++) {
            try {
                JSONObject route = originToDestinationRoutes.getJSONArray(i).getJSONObject(0); // The API response has bad formatting and what should be an Object here is returned as an Array, and thus we have the hardcoded .getJSONObject(0).

                Flight flight = new Flight();
                flight.setDepartureAirportCode(route.getString("origin_code"));
                flight.setArrivalAirportCode(route.getString("destination_code"));
                flight.setPrice(route.getDouble("regular_fare"));
                flight.setCurrency(route.getString("currency"));
                flight.setDepartureDateTime(route.getString("departure_datetime_utc"));
                flight.setArrivalDateTime(route.getString("arrival_datetime_utc"));
                availableFlights.add(flight);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sortFlightsByPriceDesc(availableFlights, 2);
    }

    /**
     * Sorts the flights by price in descending order.
     * @param flights
     * @return ArrayList<Flight> sorted by price in descending order
     */
    private ArrayList<Flight> sortFlightsByPriceDesc(ArrayList<Flight> flights, int desiredAmountOfFlights) {
        flights.sort((Flight f1, Flight f2) -> {
            if (f1.getPrice() > f2.getPrice()) {
                return 1;
            } else if (f1.getPrice() < f2.getPrice()) {
                return -1;
            } else {
                return 0;
            }
        });

        for (int i = 0; i < desiredAmountOfFlights; i++) {
            if(i >= desiredAmountOfFlights) {
                flights.remove(i);
            }
        }

        return flights;
    }


}
