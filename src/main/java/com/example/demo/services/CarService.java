package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.LocationEntity;
import com.example.demo.enums.Category;
import com.example.demo.enums.Transmission;
import com.example.demo.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarService implements ICarService {
    @Autowired CarRepository carRepository;

    @Override
    public CarEntity createCar (String name, String registration, String manufacturer,
                                String model, Transmission transmission, Category category,
                                LocationEntity location) {

        CarEntity newCar = new CarEntity(name, registration, manufacturer, model, transmission, category, location);
        carRepository.save(newCar);
        return newCar;
    }

    @Override
    public void deleteCar (Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public Optional<CarEntity> findById (Long id) {
        return carRepository.findById(id);
    }

    @Override
    public Iterable<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Page<CarEntity> findAll (Pageable pageable) {
        return carRepository.findAll(pageable);
    }
}
