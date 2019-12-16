package com.example.demo.entities;

import lombok.Getter;
import javax.persistence.*;

@javax.persistence.Entity
@Table(name = "customer")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Getter public long id;
    @Getter private String name;

    public CustomerEntity(){};

    public CustomerEntity(String name) {
        this.name = name;
    }

}
