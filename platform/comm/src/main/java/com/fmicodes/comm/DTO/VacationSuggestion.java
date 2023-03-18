package com.fmicodes.comm.DTO;

import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.DTO.travel.Flight;

import java.util.ArrayList;

public class VacationSuggestion {
    private ArrayList<Hotel> hotelSuggestions;

    private ArrayList<Flight> flightSuggestions;

    public ArrayList<Hotel> getHotelSuggestions() {
        return hotelSuggestions;
    }

    public void setHotelSuggestions(ArrayList<Hotel> hotelSuggestions) {
        this.hotelSuggestions = hotelSuggestions;
    }

    public ArrayList<Flight> getFlightSuggestions() {
        return flightSuggestions;
    }

    public void setFlightSuggestions(ArrayList<Flight> flightSuggestions) {
        this.flightSuggestions = flightSuggestions;
    }
}
