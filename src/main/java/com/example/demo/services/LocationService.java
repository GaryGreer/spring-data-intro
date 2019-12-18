package com.example.demo.services;

import com.example.demo.controllers.LocationController;
import com.example.demo.entities.LocationEntity;
import com.example.demo.error.IdNotFoundException;
import com.example.demo.error.LocationExistsException;
import com.example.demo.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.net.URI;
import java.util.Optional;

@Service
public class LocationService implements ILocationService, Serializable {

    @Autowired LocationRepository locationRepository;

    @Override
    public ResponseEntity createLocation(LocationEntity location) {
        LocationEntity loc = locationRepository.findOneByLocation(location.getLocation());
        if (loc == null) {
            locationRepository.save(location);
            URI u = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(LocationController.class).findLocation(location.getId())).toUri();
            return ResponseEntity.ok(u);
        } else{
            throw new LocationExistsException(location.getLocation());
        }
    }

    @Override
    public ResponseEntity<String> deleteLocation(Long id) {
        Optional<LocationEntity> deleteLocation = locationRepository.findById(id);
        if (deleteLocation.isPresent()){
            locationRepository.deleteById(id);
            return new ResponseEntity<>("location deleted successfully.", new HttpHeaders(), HttpStatus.NO_CONTENT);
        } else {
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public ResponseEntity<LocationEntity> findById(Long id) {

        Optional<LocationEntity> foundLocation = locationRepository.findById(id);
        if (foundLocation.isPresent()){
            return new ResponseEntity<>(foundLocation.get(), HttpStatus.OK);
        } else{
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public Iterable<LocationEntity> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Page<LocationEntity> findAll(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }
}
