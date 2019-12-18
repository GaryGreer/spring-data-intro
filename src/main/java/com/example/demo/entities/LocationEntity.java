package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Table(name = "locations")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long id;
    @NonNull private String location;

    @JsonIgnoreProperties("cars")
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "location", fetch = FetchType.LAZY)
    @Getter private Set<CarEntity> cars = new HashSet<>();
}
