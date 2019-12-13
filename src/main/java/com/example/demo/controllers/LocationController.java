package com.example.demo.controllers;

import com.example.demo.entities.LocationEntity;
import com.example.demo.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import org.springframework.web.util.UriComponentsBuilder;

@RestController("/api/location")
@RequestMapping(value = "/api/location")
public class LocationController {

    @Autowired LocationService locationService;

    @PutMapping(path = "/addLocation")
    public ResponseEntity<LocationEntity> addLocation(@RequestParam(value="location") String location, UriComponentsBuilder ucBuilder){
        HttpHeaders headers = new HttpHeaders();

        Iterable<LocationEntity> allLocations = locationService.getAllLocations();
        for ( LocationEntity loc : allLocations ){
            if (loc.getLocation().equalsIgnoreCase(location)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "location already exists.");
            }
        }
        LocationEntity newLocation = locationService.createLocation(location);
        headers.setLocation(ucBuilder.path("/api/location/addLocation/{location}").buildAndExpand(newLocation.getLocation()).toUri());
        return new ResponseEntity<>(newLocation, headers, HttpStatus.CREATED);
    }

    @GetMapping(path = "/findById")
    public ResponseEntity<LocationEntity> findLocation(@RequestParam("id") Long id){
        Optional<LocationEntity> foundLocation = locationService.findById(id);
        if (foundLocation.isPresent()){
            return new ResponseEntity<>(foundLocation.get(), HttpStatus.OK);
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found.");
        }
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<String> deleteLocation(@RequestParam("id") Long id){
        HttpHeaders headers = new HttpHeaders();
        Optional<LocationEntity> deleteLocation = locationService.findById(id);
        if (deleteLocation.isPresent()){
            locationService.deleteLocation(id);
            return new ResponseEntity<>("location deleted successfully.", headers, HttpStatus.NO_CONTENT);
        } else {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found.");
        }
    }

    @GetMapping(path = "/listPageable")
    public ResponseEntity<Page> locationPageable(Pageable pageable){
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(locationService.findAll(pageable), headers, HttpStatus.OK);
    }
}
