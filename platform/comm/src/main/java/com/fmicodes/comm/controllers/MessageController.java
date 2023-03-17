package com.fmicodes.comm.controllers;

import com.fmicodes.comm.DTO.VacationDescription;
import com.fmicodes.comm.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<String> makeVacationSuggestion(@RequestBody VacationDescription vacationDescription) {
        // Call python code to read gpt's output. Python code receives string and returns a map

        // based on python code response, make API calls to: bookingAPI, skyscannerAPI and googleMapsAPI

        // return 3-4 VacationSuggestion objects with data for: location (city, country), hotels, flights and attractions

        messageService.getHotelsByParams();

        return ResponseEntity.ok("Hello World, " + vacationDescription.getVacationDescription());
    }

}
