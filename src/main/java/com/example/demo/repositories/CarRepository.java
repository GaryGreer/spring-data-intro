package com.example.demo.repositories;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<CarEntity, Long> {
    CarEntity findOneByRegistration(String registration);
    List<CarEntity> findAllByLocation(LocationEntity location);
}
