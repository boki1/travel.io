package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.Location;
import com.fmicodes.comm.DTO.OpenAIDestinationResponse;
import com.fmicodes.comm.DTO.RestaurantInfo;
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

    private static final String rapidApiKey = CredentialsUtil.getRapidAPIKey();
    @Autowired
    private AnalyzerService analyzerService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private RyanAirService ryanAirService;
    @Autowired
    private GoogleMapsService googleMapsService;

    public ArrayList<OpenAIDestinationResponse> getMessageAnalysis(String message) {
        return analyzerService.analyzeMessage(message);
    }

    public ArrayList<Hotel> getHotelsByParams(Location location, String checkInDate, String checkOutDate, Double maximumBudget) {
        String airportIATACode = analyzerService.getAirportIATACodeByLocation(location);
        return bookingService.getHotelsByParams(location, checkInDate, checkOutDate, maximumBudget, airportIATACode);
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
                String description = locationArray.getString(2);
                Location location = new Location(city, country, description);

                locationData.add(location);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return locationData;
    }

    public ArrayList<VacationOffer> bundleVacationOffers(ArrayList<Hotel> hotels, Location departureLocation, String departureDate, String returnDate, ArrayList<String> landmarks, ArrayList<String> suggestedActivities) {
        String originAirportCode = analyzerService.getAirportIATACodeByLocation(departureLocation);

        Flight flight = null;
        if (hotels.size() != 0) {
            flight = ryanAirService.getFlightBetweenTwoAirports(originAirportCode,
                    hotels.get(0).getAirportCode(), departureDate, returnDate);
        }

        ArrayList<VacationOffer> vacationOffers = new ArrayList<>();
        for (Hotel hotel : hotels) {
            hotel.setSuggestedActivities(suggestedActivities);
            System.out.println("HOTEL: " + hotel.getHotelName() + " AIRPORT CODE: " + hotel.getAirportCode());

            hotel.setNearbyRestaurants(googleMapsService.getNearbyRestaurants(hotel.getLatitude(), hotel.getLongitude(), 500));

            System.out.println("HOTEL DATA: " + hotel);

            VacationOffer vacationOffer = new VacationOffer();
            vacationOffer.setHotel(hotel);
            vacationOffer.setFlight(flight);
            vacationOffers.add(vacationOffer);
        }

        return vacationOffers;
    }


}
