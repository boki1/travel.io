package com.fmicodes.comm.DTO.booking;

import com.fmicodes.comm.DTO.RestaurantInfo;

import java.util.ArrayList;

public class Hotel {

    private Integer hotelId;
    private String hotelName;

    private Double reviewScore;

    private String address;

    private String maxPhotoUrl;

    private String url;

    private String airportCode;

    private Double longitude;

    private Double latitude;

    private Double price;

    private String currency;

    private ArrayList<RestaurantInfo> nearbyRestaurants;

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Double getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(Double reviewScore) {
        this.reviewScore = reviewScore;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaxPhotoUrl() {
        return maxPhotoUrl;
    }

    public void setMaxPhotoUrl(String maxPhotoUrl) {
        this.maxPhotoUrl = maxPhotoUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
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

    public ArrayList<RestaurantInfo> getNearbyRestaurants() {
        return nearbyRestaurants;
    }

    public void setNearbyRestaurants(ArrayList<RestaurantInfo> nearbyRestaurants) {
        this.nearbyRestaurants = nearbyRestaurants;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelId=" + hotelId +
                ", hotelName='" + hotelName + '\'' +
                ", reviewScore=" + reviewScore +
                ", address='" + address + '\'' +
                ", maxPhotoUrl='" + maxPhotoUrl + '\'' +
                ", url='" + url + '\'' +
                ", airportCode='" + airportCode + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
