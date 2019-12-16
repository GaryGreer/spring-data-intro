package com.example.demo.controllers;

import com.example.demo.entities.LocationEntity;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController("/api/location")
@RequestMapping(value = "/api/location")
public class LocationController {

    @Autowired LocationService locationService;

    @PostMapping(path = "/")
    public ResponseEntity<LocationEntity> addLocation(@RequestBody LocationEntity location){
        return locationService.createLocation(location);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<LocationEntity> findLocation(@PathVariable("id") Long id){
        return locationService.findById(id);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable("id") Long id){
        return locationService.deleteLocation(id);
    }

    @GetMapping(path = "/")
    public Page locationPageable(Pageable pageable){
       return locationService.findAll(pageable);
    }
}
