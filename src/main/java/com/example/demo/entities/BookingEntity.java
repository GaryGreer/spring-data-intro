package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@javax.persistence.Entity
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter
    public long id;
    @Getter private LocalDate startDate;
    @Getter private LocalDate endDate;

    @JsonIgnoreProperties("bookings")
    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter private CarEntity car;

    @OneToOne(optional = false)
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter private CustomerEntity customer;

    public BookingEntity(){}

    public BookingEntity(LocalDate startDate, LocalDate endDate, CarEntity car, CustomerEntity customer){
        this.startDate = startDate;
        this.endDate = endDate;
        this.car = car;
        this.customer = customer;
    }

}
