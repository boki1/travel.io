package com.fmicodes.comm.DTO.mapsAPI;

public class Landmark {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String openingHours;
    private Float rating;
    private Boolean openNow;

    public Landmark(String name,
                    String address,
                    Double latitude,
                    Double longitude,
                    String openingHours,
                    Float rating,
                    Boolean openNow) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openingHours = openingHours;
        this.rating = rating;
        this.openNow = openNow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }
}
