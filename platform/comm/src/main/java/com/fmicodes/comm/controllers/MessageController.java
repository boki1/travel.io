package com.fmicodes.comm.controllers;

import com.fmicodes.comm.DTO.ErrorResponse;
import com.fmicodes.comm.DTO.VacationDescription;
import com.fmicodes.comm.DTO.VacationOffer;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping
    public ResponseEntity<VacationSuggestion> makeVacationSuggestion(@RequestBody VacationDescription vacationDescription) {
        String cityMock = "Zagreb";
        String countryMock = "Croatia";

        String analyzerResponse = messageService.getMessageAnalysis(vacationDescription.getVacationDescription());

        ArrayList<Hotel> hotelSuggestions = messageService.getHotelsByParams(cityMock, countryMock, vacationDescription.getCheckInDate(), vacationDescription.getCheckOutDate(), vacationDescription.getMaxPrice());

        ArrayList<VacationOffer> vacationOffers = messageService.bundleVacationOffers(hotelSuggestions, vacationDescription.getCheckInDate());


        VacationSuggestion vacationSuggestions = new VacationSuggestion();
        vacationSuggestions.setVacationOffers(vacationOffers);


        return new ResponseEntity<>(vacationSuggestions, null, HttpStatus.OK);
    }

}
