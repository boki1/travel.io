package com.fmicodes.comm.DTO;

import com.fmicodes.comm.DTO.booking.Hotel;

import java.util.ArrayList;

public class VacationSuggestion {
    private ArrayList<Hotel> hotelSuggestions;

    public ArrayList<Hotel> getHotelSuggestions() {
        return hotelSuggestions;
    }

    public void setHotelSuggestions(ArrayList<Hotel> hotelSuggestions) {
        this.hotelSuggestions = hotelSuggestions;
    }
}
