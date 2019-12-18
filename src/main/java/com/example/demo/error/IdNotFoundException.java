package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(Long id){
        super("id not found:" + id);
    }

    public IdNotFoundException(Long id, Class cl){
        super( cl.getName() +" id not found:" + id);
    }
}
