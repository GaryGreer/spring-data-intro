package com.example.demo.services;

import com.example.demo.entities.LocationEntity;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ILocationService {

    ResponseEntity<LocationEntity> findById(Long id, Boolean entity);
    ResponseEntity createLocation(LocationEntity location);
    ResponseEntity<String> deleteLocation(Long id) throws NotFoundException;
    ResponseEntity getAllLocations();
    Page<LocationEntity> findAll(Pageable pageable);
}
