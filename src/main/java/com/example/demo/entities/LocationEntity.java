package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@javax.persistence.Entity
@NamedEntityGraph(
        name = "location-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("location")
        }
)
@Table(name = "locations")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter public long id;
    @Getter private String location;

    public LocationEntity(){};

    public LocationEntity(String location) {
        this.location = location;
    }

    @JsonIgnoreProperties("cars")
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "location", fetch = FetchType.LAZY)
    @Getter private Set<CarEntity> cars = new HashSet<>();
}
