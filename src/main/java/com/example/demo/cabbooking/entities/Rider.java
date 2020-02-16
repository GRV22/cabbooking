package com.example.demo.cabbooking.entities;

import lombok.Data;

@Data
public class Rider extends Person {
    private final int riderId;
    private Location lastLocation;
    private double balance;

    public Rider(int nextId) {
        this.riderId = nextId;
    }
}
