package com.example.demo.services;

import com.example.demo.entities.LocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ILocationService {

    Optional<LocationEntity> findById(Long id);
    LocationEntity createLocation(String location);
    void deleteLocation(Long id);
    Iterable<LocationEntity> getAllLocations();
    Page<LocationEntity> findAll(Pageable pageable);
}
