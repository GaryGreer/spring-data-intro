package com.example.demo.repositories;

import com.example.demo.entities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    LocationEntity findOneByLocation(String name);
}

