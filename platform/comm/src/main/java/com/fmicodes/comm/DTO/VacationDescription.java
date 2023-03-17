package com.fmicodes.comm.DTO;

public class VacationDescription {
    private String vacationDescription;

    private Double minPrice;

    private Double maxPrice;

    private String currentLocation;

    public String getVacationDescription() {
        return vacationDescription;
    }

    public void setVacationDescription(String vacationDescription) {
        this.vacationDescription = vacationDescription;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}
