package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.LocationEntity;
import com.example.demo.enums.Category;
import com.example.demo.enums.Transmission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ICarService {

    Optional<CarEntity> findById(Long id);
    CarEntity createCar(String name, String registration, String manufacturer,
                        String model, Transmission transmission, Category category,
                        LocationEntity location);
    void deleteCar(Long id);
    Iterable<CarEntity> getAllCars();
    Page<CarEntity> findAll(Pageable pageable);
}
