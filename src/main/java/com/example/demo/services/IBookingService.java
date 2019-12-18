package com.example.demo.services;

import com.example.demo.entities.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public interface IBookingService {

    ResponseEntity<BookingEntity> findById(Long id);
    ResponseEntity<URI> createBooking(BookingEntity booking);
    ResponseEntity<String> deleteBooking(Long id);
    Iterable<BookingEntity> getAllBookings();
    Page findAll(Pageable pageable);
    Page findTodays(Pageable pageable);
}
