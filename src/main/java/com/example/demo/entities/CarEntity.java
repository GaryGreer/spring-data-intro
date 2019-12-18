package com.example.demo.entities;

import com.example.demo.enums.Category;
import com.example.demo.enums.Transmission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "car")
public class CarEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter public long id;
    @Getter private String name;
    @Getter private String registration;
    @Getter private String manufacturer;
    @Getter private String model;
    @Enumerated(EnumType.STRING) @Getter private Transmission transmission;
    @Enumerated(EnumType.STRING) @Getter private Category category;

    @JsonIgnoreProperties("cars")
    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Getter private LocationEntity location;

    //@JsonIgnoreProperties("bookings")
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "car", fetch = FetchType.LAZY)
    @Getter private Set<BookingEntity> bookings = new HashSet<>();

    public CarEntity(){}

    public CarEntity(String name, String registration, String manufacturer,
                     String model, Transmission transmission, Category category,
                     LocationEntity location){
        this.name = name;
        this.registration = registration;
        this.manufacturer = manufacturer;
        this.model = model;
        this.transmission = transmission;
        this.category = category;
        this.location = location;
    }
}