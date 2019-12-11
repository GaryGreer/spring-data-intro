package com.example.demo.services;

import com.example.demo.entities.LocationEntity;
import com.example.demo.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService implements ILocationService {

    @Autowired LocationRepository locationRepository;

    @Override
    public LocationEntity createLocation(String location) {
        LocationEntity newLocation = new LocationEntity(location);
        locationRepository.save(newLocation);
        return newLocation;
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public Optional<LocationEntity> findById(Long id) {
        return locationRepository.findById(id);
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
