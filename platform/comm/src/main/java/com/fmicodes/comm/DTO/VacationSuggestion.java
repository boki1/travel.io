package com.fmicodes.comm.DTO;

import com.fmicodes.comm.DTO.mapsAPI.Landmark;

import java.util.ArrayList;
import java.util.List;

public class VacationSuggestion {

    private ArrayList<VacationOffer> vacationOffers;
    private List<Landmark> landmarks;
    private Location location;
    List<String> activities;

    private String cardDisplayImage;

    public ArrayList<VacationOffer> getVacationOffers() {
        return vacationOffers;
    }

    public void setVacationOffers(ArrayList<VacationOffer> vacationOffers) {
        this.vacationOffers = vacationOffers;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(List<Landmark> landmarks) {
        this.landmarks = landmarks;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public String getCardDisplayImage() {
        return cardDisplayImage;
    }

    public void setCardDisplayImage(String cardDisplayImage) {
        this.cardDisplayImage = cardDisplayImage;
    }
}
