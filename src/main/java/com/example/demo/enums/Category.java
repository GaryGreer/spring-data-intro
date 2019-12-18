package com.example.demo.enums;

public enum Category {
    PETROL ("Petrol"),
    DIESEL ("Diesel"),
    ELECTRIC ("Electric"),
    HYBRID ("Hybrid");

    private String value;

    private Category(String value) {
        this.value = value;
    }
}
