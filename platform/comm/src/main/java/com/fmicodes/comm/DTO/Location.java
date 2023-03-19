package com.fmicodes.comm.DTO;

public class Location {
    private String city;
    private String country;
    private String description;

    public Location(String city, String country, String description) {
        this.city = city;
        this.country = country;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
