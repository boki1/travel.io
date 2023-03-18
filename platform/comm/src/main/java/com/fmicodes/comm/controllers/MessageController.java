package com.fmicodes.comm.controllers;

import com.fmicodes.comm.DTO.VacationDescription;
import com.fmicodes.comm.DTO.VacationSuggestion;
import com.fmicodes.comm.DTO.booking.Hotel;
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
        String cityMock = "London";
        String countryMock = "United Kingdom";

//        String analyzerResponse = messageService.getMessageAnalysis(vacationDescription.getVacationDescription());

        VacationSuggestion vacationSuggestions = new VacationSuggestion();

        ArrayList<Hotel> hotelSuggestions = messageService.getHotelsByParams(cityMock, countryMock);
        vacationSuggestions.setHotelSuggestions(hotelSuggestions);

        return new ResponseEntity<>(vacationSuggestions, null, HttpStatus.OK);
    }

}
