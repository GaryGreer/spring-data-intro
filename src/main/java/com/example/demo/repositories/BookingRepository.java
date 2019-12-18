package com.example.demo.repositories;

import com.example.demo.entities.BookingEntity;
import com.example.demo.entities.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findAllByCar(CarEntity car);
}
