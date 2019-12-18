package com.example.demo.repositories;

import com.example.demo.entities.LocationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    LocationEntity findOneByLocation(String location);
    @EntityGraph(value = "location-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    LocationEntity findOneById(@Param("id") Long id);
}

