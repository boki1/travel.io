package com.fmicodes.comm.controllers;

import com.fmicodes.comm.DTO.*;
import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping
    public ResponseEntity<ArrayList<VacationSuggestion>> makeVacationSuggestion(@RequestBody VacationDescription vacationDescription) {

        String analyzerResponse = messageService.getMessageAnalysis(vacationDescription.getVacationDescription());

        ArrayList<Location> locationData = messageService.getLocationDataFromOpenAIResponse(analyzerResponse);

        locationData = (ArrayList<Location>) locationData.stream().limit(2).collect(Collectors.toList());

        ArrayList<VacationSuggestion> vacationSuggestions = new ArrayList<>();
//        for (Location location : locationData) {
        Location location = locationData.get(0);
            ArrayList<Hotel> hotelSuggestions = messageService.getHotelsByParams(location.getCity(), location.getCountry(), vacationDescription.getCheckInDate(), vacationDescription.getCheckOutDate(), vacationDescription.getMaxPrice());

            ArrayList<VacationOffer> vacationOffers = messageService.bundleVacationOffers(hotelSuggestions, vacationDescription.getCheckInDate());

            VacationSuggestion vacationSuggestion = new VacationSuggestion();
            vacationSuggestion.setLocation(location);
            vacationSuggestion.setVacationOffers(vacationOffers);

            vacationSuggestions.add(vacationSuggestion);
//        }

        return new ResponseEntity<>(vacationSuggestions, null, HttpStatus.OK);
    }

}
