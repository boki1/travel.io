package com.fmicodes.comm.DTO.booking;

public class Hotel {

    private Integer hotelId;
    private String hotelName;

    private Double reviewScore;

    private String address;

    private String maxPhotoUrl;

    private String url;


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

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelId=" + hotelId +
                ", hotelName='" + hotelName + '\'' +
                ", reviewScore=" + reviewScore +
                ", address='" + address + '\'' +
                ", maxPhotoUrl='" + maxPhotoUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
