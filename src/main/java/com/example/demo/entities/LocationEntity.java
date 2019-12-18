package com.example.demo.entities;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "locations")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long id;

    private String location;

    public LocationEntity(){};

    public LocationEntity(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
    public long getId() {
        return id;
    }
}
