package com.example.demo.entities;

import lombok.Getter;
import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "location")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter public long id;
    @Getter private String location;

    public LocationEntity(){};

    public LocationEntity(String location) {
        this.location = location;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "location", fetch = FetchType.EAGER)
    Set<CarEntity> cars;
}
