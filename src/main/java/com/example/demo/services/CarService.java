package com.example.demo.services;

import com.example.demo.controllers.CarController;
import com.example.demo.entities.CarEntity;
import com.example.demo.entities.LocationEntity;
import com.example.demo.error.CarExistsException;
import com.example.demo.error.IdNotFoundException;
import com.example.demo.repositories.CarRepository;
import com.example.demo.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class CarService implements ICarService {
    @Autowired CarRepository carRepository;
    @Autowired LocationRepository locationRepository;

    @Override public ResponseEntity createCar(CarEntity car) {
        CarEntity addCar = carRepository.findOneByRegistration(car.getRegistration());
        if (addCar == null) {
            carRepository.save(car);
            URI u = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CarController.class).findCustomer(car.getId())).toUri();
            return ResponseEntity.ok(u);
        } else{
            throw new CarExistsException(car.getRegistration());
        }
    }

    @Override
    public ResponseEntity deleteCar (Long id) {
        Optional<CarEntity> deleteCar = carRepository.findById(id);
        if (deleteCar.isPresent()){
            carRepository.deleteById(id);
            return new ResponseEntity("car deleted successfully.", new HttpHeaders(), HttpStatus.NO_CONTENT);
        } else {
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public ResponseEntity findById (Long id) {
        Optional<CarEntity> foundCar = carRepository.findById(id);
        if (foundCar.isPresent()){
            return new ResponseEntity(foundCar.get(), HttpStatus.OK);
        } else{
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public Iterable<CarEntity> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Page<CarEntity> findAll (Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    @Override
    public List<CarEntity> findByLocation(Long location) {
        LocationEntity loc = locationRepository.findById(location).get();
        return carRepository.findAllByLocation(loc);

        /*
        Optional<LocationEntity> loc = locationRepository.findById(location);
        if (loc.isPresent()){
            return new ResponseEntity(carRepository.findAllByLocation(loc.get()), HttpStatus.OK);
        } else{
            throw new IdNotFoundException(location);
        }
         */
    }

}
