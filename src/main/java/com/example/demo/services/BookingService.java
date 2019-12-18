package com.example.demo.services;

import com.example.demo.controllers.BookingController;
import com.example.demo.entities.BookingEntity;
import com.example.demo.entities.CarEntity;
import com.example.demo.entities.CustomerEntity;
import com.example.demo.entities.LocationEntity;
import com.example.demo.error.IdNotFoundException;
import com.example.demo.error.IllegalBookingException;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.CarRepository;
import com.example.demo.repositories.CustomerRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService implements IBookingService {
    @Autowired BookingRepository bookingRepository;
    @Autowired CarRepository carRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired LocationRepository locationRepository;

    @Override
    public ResponseEntity createBooking(BookingEntity booking) {
        Optional<CarEntity> car = carRepository.findById(booking.getCar().getId());
        Optional<CustomerEntity> customer = customerRepository.findById(booking.getCustomer().getId());
        Optional<LocationEntity> location = locationRepository.findById(booking.getCar().getLocation().getId());

        if (car.isPresent() && customer.isPresent() && location.isPresent()){
            List<BookingEntity> bookings = bookingRepository.findAllByCar(car.get());
            for (BookingEntity b: bookings){
                if ((booking.getStartDate().isBefore(b.getEndDate()) && b.getStartDate().isBefore(booking.getEndDate()))){
                    throw new IllegalBookingException(booking.getCar().getId(), booking.getStartDate(), booking.getEndDate());
                }
            }
            bookingRepository.save(booking);
            URI u = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BookingController.class).findBooking(booking.getId())).toUri();
            return ResponseEntity.ok(u);
        } else {
            if (!car.isPresent()){
                throw new IdNotFoundException(booking.getCar().getId(), booking.getCar().getClass());
            } else if(!customer.isPresent()){
                throw new IdNotFoundException(booking.getCustomer().getId(), booking.getCustomer().getClass());
            } else{
                throw new IdNotFoundException(booking.getCar().getLocation().getId(), booking.getCar().getLocation().getClass());
            }
        }
    }

    @Override
    public ResponseEntity findById (Long id) {
        Optional<BookingEntity> foundCar = bookingRepository.findById(id);
        if (foundCar.isPresent()){
            return new ResponseEntity(foundCar.get(), HttpStatus.OK);
        } else{
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public ResponseEntity<String> deleteBooking(Long id) {
        Optional<BookingEntity> deleteBooking = bookingRepository.findById(id);
        if (deleteBooking.isPresent()){
            bookingRepository.deleteById(id);
            return new ResponseEntity("booking deleted successfully.", new HttpHeaders(), HttpStatus.NO_CONTENT);
        } else {
            throw new IdNotFoundException(id);
        }
    }

    @Override
    public Iterable<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Page findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Override
    public Page findTodays(Pageable pageable) {
        return bookingRepository.findAllByStartDate(LocalDate.now(), pageable);
    }
}
