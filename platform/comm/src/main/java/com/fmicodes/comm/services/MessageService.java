package com.fmicodes.comm.services;

import com.fmicodes.comm.DTO.VacationOffer;
import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.DTO.travel.Flight;
import com.fmicodes.comm.exceptions.AirportCompatibilityException;
import com.fmicodes.comm.exceptions.DeserializingJSONException;
import com.fmicodes.comm.services.util.CredentialsUtil;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
