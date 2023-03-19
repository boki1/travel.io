package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.*;
import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.DTO.travel.Flight;
import com.fmicodes.comm.services.util.CredentialsUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

    public ArrayList<VacationOffer> bundleVacationOffers(ArrayList<Hotel> hotels,
                                                         Location departureLocation,
                                                         String departureDate,
                                                         String returnDate,
                                                         ArrayList<String> suggestedActivities) {
        String originAirportCode = analyzerService.getAirportIATACodeByLocation(departureLocation);

        Flight flight = null;
        if (!hotels.isEmpty()) {
            flight = ryanAirService.getFlightBetweenTwoAirports(originAirportCode,
                    hotels.get(0).getAirportCode(), departureDate, returnDate);
        }

        ArrayList<VacationOffer> vacationOffers = new ArrayList<>();
        for (Hotel hotel : hotels) {
            hotel.setSuggestedActivities(suggestedActivities);
            hotel.setNearbyRestaurants(googleMapsService.getNearbyRestaurants(hotel.getLatitude(), hotel.getLongitude(), 500));

            VacationOffer vacationOffer = new VacationOffer();
            vacationOffer.setHotel(hotel);
            vacationOffer.setFlight(flight);

            vacationOffers.add(vacationOffer);
        }



        return vacationOffers;
    }


    public VacationSuggestion prepareVacationSuggestion(Location locationData,
                                                        ArrayList<VacationOffer> vacationOffers,
                                                        ArrayList<String> landmarks,
                                                        ArrayList<String> activities) {
        VacationSuggestion vacationSuggestion = new VacationSuggestion();

        vacationSuggestion.setLocation(locationData);
        vacationSuggestion.setVacationOffers(vacationOffers);
        vacationSuggestion.setLandmarks(googleMapsService.searchLandmarks(landmarks, locationData));
        vacationSuggestion.setActivities(activities);

        return vacationSuggestion;
    }
}
