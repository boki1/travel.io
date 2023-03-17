package com.fmicodes.comm.controllers;

import com.fmicodes.comm.DTO.VacationDescription;
import com.fmicodes.comm.DTO.VacationSuggestion;
import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
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
        // Call python code to read gpt's output. Python code receives string and returns a map

        // based on python code response, make API calls to: bookingAPI, skyscannerAPI and googleMapsAPI

        // return 3-4 VacationSuggestion objects with data for: location (city, country), hotels, flights and attractions

        String mockKrisResponse = "{\n" +
                "  \"Destination\": {\n" +
                "    \"city\": \"Istanbul\",\n" +
                "    \"country\": \"Turkey\"\n" +
                "  }\n" +
                "}";
        // These will be extracted from the python code response, I PROMISE
        String cityMock = "Istanbul";
        String countryMock = "Turkey";


        VacationSuggestion vacationSuggestions = new VacationSuggestion();

        ArrayList<Hotel> hotelSuggestions = messageService.getHotelsByParams(cityMock, countryMock);
        vacationSuggestions.setHotelSuggestions(hotelSuggestions);

        return new ResponseEntity<>(vacationSuggestions, null, HttpStatus.OK);
    }

}
