package com.example.demo.controllers;

import com.example.demo.entities.CarEntity;
import com.example.demo.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/car")
@RequestMapping(value = "/api/car")
public class CarController {

    @Autowired CarService carService;

    @PostMapping(path = "/")
    public ResponseEntity<CarEntity> createCar(@RequestBody CarEntity car) {
        return carService.createCar(car);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CarEntity> findCustomer(@PathVariable("id") Long id){
        return carService.findById(id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable("id") Long id){
        return carService.deleteCar(id);
    }

    @GetMapping(path = "/")
    public Page<CarEntity> carPageable(Pageable pageable){
        return carService.findAll(pageable);
    }

    @GetMapping(path = "/{id}/location")
    public List<CarEntity> carsLocation(@PathVariable("id") Long id){
        return carService.findByLocation(id);
    }
}
