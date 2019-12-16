package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LocationExistsException extends RuntimeException {
    public LocationExistsException(String location){
        super("location already exists:" + location);
    }
}
