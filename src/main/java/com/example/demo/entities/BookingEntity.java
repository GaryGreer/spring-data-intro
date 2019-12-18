package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@javax.persistence.Entity
@Table(name = "booking")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class BookingEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long id;
    @NonNull private LocalDate startDate;
    @NonNull private LocalDate endDate;

    @JsonIgnoreProperties("bookings")
    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NonNull private CarEntity car;

    @OneToOne(optional = false)
    @JoinColumn(name = "customer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NonNull private CustomerEntity customer;
}
