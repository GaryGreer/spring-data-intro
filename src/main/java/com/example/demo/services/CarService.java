package com.example.demo.services;

import com.example.demo.entities.CarEntity;
import com.example.demo.entities.LocationEntity;
import com.example.demo.enums.Category;
import com.example.demo.enums.Transmission;
import com.example.demo.repositories.CarRepository;
import com.example.demo.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class CarService implements ICarService {
    @Autowired CarRepository carRepository;
    @Autowired LocationRepository locationRepository;

    public CarEntity createCar (String name, String registration, String manufacturer,
                                String model, String transmission, String category,
                                long location_id) {

        Category car_category = null;
        Transmission car_transmission = null;

        for (Category cat : Category.values()){
            if (cat.toString().equalsIgnoreCase(category)){
                car_category = cat;
            }
        }

        for (Transmission tran : Transmission.values()){
            if (tran.toString().equalsIgnoreCase(transmission)){
                car_transmission = tran;
            }
        }

        CarEntity newCar = new CarEntity(name, registration, manufacturer, model, car_transmission, car_category, locationRepository.findById(location_id).get());


        if (car_category == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid category.");
        }
        else if (car_transmission == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "invalid transmission.");
        } else{
            return new ResponseEntity<>(newCar, headers, HttpStatus.CREATED);
        }


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
