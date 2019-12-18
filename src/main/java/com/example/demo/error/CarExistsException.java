package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CarExistsException extends RuntimeException {
    public CarExistsException(String registration){
        super("car registration already exists:" + registration);
    }
}
