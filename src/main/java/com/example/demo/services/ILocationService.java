package com.example.demo.services;

import com.example.demo.entities.LocationEntity;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ILocationService {

    ResponseEntity<LocationEntity> findById(Long id);
    ResponseEntity createLocation(LocationEntity location);
    ResponseEntity<String> deleteLocation(Long id) throws NotFoundException;
    Iterable<LocationEntity> getAllLocations();
    Page<LocationEntity> findAll(Pageable pageable);
}
