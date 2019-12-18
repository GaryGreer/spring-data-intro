package com.example.demo.entities;

import com.example.demo.enums.Category;
import com.example.demo.enums.Transmission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "car")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
public class CarEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long id;
    @NonNull private String name;
    @NonNull private String registration;
    @NonNull private String manufacturer;
    @NonNull private String model;
    @NonNull @Enumerated(EnumType.STRING) private Transmission transmission;
    @NonNull @Enumerated(EnumType.STRING) private Category category;

    @JsonIgnoreProperties("cars")
    @ManyToOne(optional = false)
    @JoinColumn(name = "location_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NonNull private LocationEntity location;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "car", fetch = FetchType.LAZY)
    private Set<BookingEntity> bookings = new HashSet<>();
}
