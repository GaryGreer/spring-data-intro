package com.example.demo.entities;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public long id;

    private String name;

    public CustomerEntity(){};

    public CustomerEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public long getId() {
        return id;
    }
}
