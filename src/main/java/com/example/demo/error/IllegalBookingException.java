package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IllegalBookingException extends RuntimeException {

    public IllegalBookingException(Long carId, LocalDate start, LocalDate end){
        super("Car " + carId + " booked between " + start + " and " + end);
    }
}