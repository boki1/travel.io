package com.fmicodes.comm.DTO;

import java.util.ArrayList;

public class OpenAIDestinationResponse {
    private Location location;

    private ArrayList<String> landmarks;

    private ArrayList<String> activities;


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<String> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(ArrayList<String> landmarks) {
        this.landmarks = landmarks;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<String> activities) {
        this.activities = activities;
    }
}
