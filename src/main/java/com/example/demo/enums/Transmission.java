package com.example.demo.enums;

public enum Transmission {
    AUTOMATIC ("Automatic"),
    MANUAL ("Manual");

    private String value;

    private Transmission(String value) {
        this.value = value;
    }
}
