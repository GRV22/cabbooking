package com.example.demo.cabbooking.entities;

import lombok.Data;

@Data
public class Location {
    public double x;
    public double y;
    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Location(Location copyLocation) {
        this.x = copyLocation.x;
        this.y = copyLocation.y;
    }
}
