package com.fmicodes.comm.DTO;

public class RestaurantInfo {
    private String name;
    private String photoUrl;
    private String location;
    private double rating;
    private Integer priceLevel;
    private int userRatingsTotal;
    private String openingHours;
    private String googleMapsLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Integer getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(Integer priceLevel) {
        this.priceLevel = priceLevel;
    }

    public int getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(int userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getGoogleMapsLink() {
        return googleMapsLink;
    }

    public void setGoogleMapsLink(String googleMapsLink) {
        this.googleMapsLink = googleMapsLink;
    }

    @Override
    public String toString() {
        return "RestaurantInfo{" +
                "name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", location='" + location + '\'' +
                ", rating=" + rating +
                ", priceLevel=" + priceLevel +
                ", userRatingsTotal=" + userRatingsTotal +
                ", openingHours='" + openingHours + '\'' +
                ", googleMapsLink='" + googleMapsLink + '\'' +
                '}';
    }
}
