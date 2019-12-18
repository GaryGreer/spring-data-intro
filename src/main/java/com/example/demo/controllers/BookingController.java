package com.example.demo.controllers;

import com.example.demo.entities.BookingEntity;
import com.example.demo.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/booking")
@RequestMapping(value = "/api/booking")
public class BookingController {

    @Autowired BookingService bookingService;

    @PostMapping(path = "/")
    public ResponseEntity<BookingEntity> createBooking(@RequestBody BookingEntity booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookingEntity> findBooking(@PathVariable("id") Long id){
        return bookingService.findById(id);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") Long id){
        return bookingService.deleteBooking(id);
    }

    @GetMapping(path = "/")
    public Page bookingPageable(Pageable pageable){
        return bookingService.findAll(pageable);
    }

    @GetMapping(path = "/today")
    public Page todayBookings(Pageable pageable){
        return bookingService.findTodays(pageable);
    }
}
