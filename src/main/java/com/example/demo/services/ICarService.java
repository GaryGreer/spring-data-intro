package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICarService {

    ResponseEntity findById(Long id);
    ResponseEntity createCar(CarEntity car);
    ResponseEntity deleteCar(Long id);
    Iterable<CarEntity> getAllCars();
    Page<CarEntity> findAll(Pageable pageable);
    List<CarEntity> findByLocation(Long id);
}
