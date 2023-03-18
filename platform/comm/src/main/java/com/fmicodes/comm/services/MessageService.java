package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.Location;
import com.fmicodes.comm.DTO.VacationOffer;
import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.DTO.travel.Flight;
import com.fmicodes.comm.services.util.CredentialsUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Destination IDs:
 * -2601889: London
 * -1746443: Berlin
 * -755070: Istanbul
 */

// TODO: do not make API calls for linking City to Airport code, because that can be done with a simple HashMap<String, ArrayList<String>> cityNameToAirportCodes

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

    public ArrayList<Hotel> getHotelsByParams(String city, String country, String checkInDate, String checkOutDate, Double maximumBudget) {
        ArrayList<Hotel> hotelSuggestions = bookingService.getHotelsByParams(city, country, checkInDate, checkOutDate, maximumBudget);
        return hotelSuggestions;
    }

    public ArrayList<Location> getLocationDataFromOpenAIResponse(String openAIResponse) {
        ArrayList<Location> locationData = new ArrayList<>();
        System.out.println("OPEN AI RESPONSE: " + openAIResponse);

        try {
            JSONObject openAIResponseJSON = new JSONObject(openAIResponse);
            JSONArray locations = openAIResponseJSON.getJSONArray("locations");
            for (int i = 0; i < locations.length(); i++) {
                JSONArray locationArray = locations.getJSONArray(i);
                String city = locationArray.getString(0);
                String country = locationArray.getString(1);
                Location location = new Location(city, country);

                locationData.add(location);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return locationData;
    }

    public ArrayList<VacationOffer> bundleVacationOffers(ArrayList<Hotel> hotels, String departureDate) {

        ArrayList<VacationOffer> vacationOffers = new ArrayList<>();
        for (Hotel hotel : hotels) {
            System.out.println("HOTEL: " + hotel.getHotelName() + " AIRPORT CODE: " + hotel.getAirportCode());
            Flight flight = ryanAirService.getFlightBetweenTwoAirports("DUB", hotel.getAirportCode(), departureDate);
            VacationOffer vacationOffer = new VacationOffer();
            vacationOffer.setHotel(hotel);
            vacationOffer.setFlight(flight);
            vacationOffers.add(vacationOffer);
        }

        return vacationOffers;
    }


}
