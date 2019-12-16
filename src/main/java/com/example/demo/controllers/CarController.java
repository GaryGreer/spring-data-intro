package com.example.demo.controllers;

import com.example.demo.entities.CarEntity;
import com.example.demo.enums.Category;
import com.example.demo.enums.Transmission;
import com.example.demo.repositories.LocationRepository;
import com.example.demo.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController("/api/car")
@RequestMapping(value = "/api/car")
public class CarController {

    @Autowired CarService carService;
    @Autowired LocationRepository locationRepository;

    @PutMapping(path = "/")
    public ResponseEntity<CarEntity> addCustomer(@RequestParam(value="name") String name,
                                                 @RequestParam(value="registration") String registration,
                                                 @RequestParam(value="manufacturer") String manufacturer,
                                                 @RequestParam(value="model") String model,
                                                 @RequestParam(value="transmission") String transmission,
                                                 @RequestParam(value="category") String category,
                                                 @RequestParam(value="location_id") long location_id,
                                                 UriComponentsBuilder ucBuilder){
        carService.createCar(name, registration, manufacturer, model, transmission, category, location_id);
    }

    @GetMapping(path = "/findById")
    public ResponseEntity<CarEntity> findCustomer(@RequestParam("id") Long id){
        Optional<CarEntity> foundCar = carService.findById(id);
        if (foundCar.isPresent()){
            return new ResponseEntity<>(foundCar.get(), HttpStatus.OK);
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found.");
        }
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<String> deleteCar(@RequestParam("id") Long id){
        HttpHeaders headers = new HttpHeaders();
        Optional<CarEntity> deleteCar = carService.findById(id);
        if (deleteCar.isPresent()){
            carService.deleteCar(id);
            return new ResponseEntity<>("customer deleted successfully.", headers, HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found.");
        }
    }

    @GetMapping(path = "/listPageable")
    public ResponseEntity<Page> carPageable(Pageable pageable){
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(carService.findAll(pageable), headers, HttpStatus.OK);
    }
}
