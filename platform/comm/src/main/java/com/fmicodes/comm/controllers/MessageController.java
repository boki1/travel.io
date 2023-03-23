package com.fmicodes.comm.controllers;

import com.fmicodes.comm.DTO.*;
import com.fmicodes.comm.DTO.booking.Hotel;
import com.fmicodes.comm.services.MessageService;
import com.fmicodes.comm.services.UnsplashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    private static final Integer MAXIMUM_NUMBER_OF_LOCATIONS = 4;
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
        ArrayList<OpenAIDestinationResponse> analyzerResponse = messageService.getMessageAnalysis(vacationDescription.getVacationDescription());
        Location currentLocation = new Location(vacationDescription.getCurrentCity(),
                vacationDescription.getCurrentCountry(),
                vacationDescription.getVacationDescription());

        analyzerResponse = analyzerResponse
                .stream()
                .limit(MAXIMUM_NUMBER_OF_LOCATIONS)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<VacationSuggestion> vacationSuggestions = new ArrayList<>();
        for (OpenAIDestinationResponse destination : analyzerResponse) {
            Location locationData = destination.getLocation();


            ArrayList<Hotel> hotelSuggestions = messageService.getHotelsByParams(locationData,
                    vacationDescription.getCheckInDate(), vacationDescription.getCheckOutDate(), vacationDescription.getMaxPrice());

            ArrayList<VacationOffer> vacationOffers = messageService.bundleVacationOffers(hotelSuggestions,
                    currentLocation, vacationDescription.getCheckInDate(), vacationDescription.getCheckOutDate(),
                    destination.getActivities());

            VacationSuggestion vacationSuggestion = messageService.prepareVacationSuggestion(locationData, vacationOffers,
                    destination.getLandmarks(), destination.getActivities());

            vacationSuggestions.add(vacationSuggestion);
        }
        return new ResponseEntity<>(vacationSuggestions, null, HttpStatus.OK);
    }

}
