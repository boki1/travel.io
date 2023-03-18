package com.fmicodes.comm.DTO;

import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.DTO.travel.Flight;

import java.util.ArrayList;

public class VacationOffer {
    private Hotel hotel;

    private ArrayList<Flight> possibleFLights;


    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public ArrayList<Flight> getPossibleFLights() {
        return possibleFLights;
    }

    public void setPossibleFLights(ArrayList<Flight> possibleFLights) {
        this.possibleFLights = possibleFLights;
    }
}
