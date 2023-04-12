package com.fmicodes.comm.DTO.travel;

import com.fmicodes.comm.DTO.Location;

public class Flight {

    private String departureAirportCode;

    private String arrivalAirportCode;
    private Double price;
    private String currency;
    private String departureDateTime;
    private String arrivalDateTime;

    private Location landingLocation;

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public Location getLandingLocation() {
        return landingLocation;
    }

    public void setLandingLocation(Location landingLocation) {
        this.landingLocation = landingLocation;
    }
}
