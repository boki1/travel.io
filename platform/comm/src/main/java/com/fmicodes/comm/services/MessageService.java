package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.DTO.travel.Flight;
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

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RyanAirService ryanAirService;

    private static String rapidApiKey = CredentialsUtil.getRapidAPIKey();

    public String getMessageAnalysis(String message) {
        String analyzerResponse = analyzerService.analyzeMessage(message);
        return analyzerResponse;
    }

    public ArrayList<Hotel> getHotelsByParams(String city, String country) {
        ArrayList<Hotel> hotelSuggestions = bookingService.getHotelsByParams(city, country);
        return hotelSuggestions;
    }

    public ArrayList<Flight> getAirplaneRoutesByParams(String locationAirportCode, String destinationAirportCode, String originDepartureDate) {
        ArrayList<Flight> routesResponse = ryanAirService.getFlightsBetweenTwoAirports(locationAirportCode, destinationAirportCode, originDepartureDate);
        return routesResponse;
    }


}
