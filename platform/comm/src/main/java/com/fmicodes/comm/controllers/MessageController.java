package com.fmicodes.comm.controllers;

import com.fmicodes.comm.DTO.VacationDescription;
import com.fmicodes.comm.DTO.VacationOffer;
import com.fmicodes.comm.DTO.VacationSuggestion;
import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.DTO.travel.Flight;
import com.fmicodes.comm.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<VacationSuggestion> makeVacationSuggestion(@RequestBody VacationDescription vacationDescription) {
        String cityMock = "Bratislava";
        String countryMock = "Slovakia";

//        String analyzerResponse = messageService.getMessageAnalysis(vacationDescription.getVacationDescription());

        ArrayList<Hotel> hotelSuggestions = messageService.getHotelsByParams(cityMock, countryMock);

//        ArrayList<Flight> routesResponse = messageService.getAirplaneRoutesByParams("LGW", "DUB", "2023-11-28");
//        vacationSuggestions.setFlightSuggestions(routesResponse);

        ArrayList<VacationOffer> vacationOffers = messageService.bundleVacationOffers(hotelSuggestions);


        VacationSuggestion vacationSuggestions = new VacationSuggestion();
        vacationSuggestions.setVacationOffers(vacationOffers);


        return new ResponseEntity<>(vacationSuggestions, null, HttpStatus.OK);
    }

}
