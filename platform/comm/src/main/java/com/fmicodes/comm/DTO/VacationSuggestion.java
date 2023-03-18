package com.fmicodes.comm.DTO;

import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.DTO.travel.Flight;

import java.util.ArrayList;

public class VacationSuggestion {

    private ArrayList<VacationOffer> vacationOffers;

    public ArrayList<VacationOffer> getVacationOffers() {
        return vacationOffers;
    }

    public void setVacationOffers(ArrayList<VacationOffer> vacationOffers) {
        this.vacationOffers = vacationOffers;
    }
}
