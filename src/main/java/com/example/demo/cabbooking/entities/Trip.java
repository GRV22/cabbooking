package com.example.demo.cabbooking.entities;

import com.example.demo.cabbooking.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class Trip {
    private int tripId;
    @Setter
    private int riderId;
    @Setter
    private int driverId;
    @Setter
    private Location startLocation;
    @Setter
    private Location endLocation;
    @Setter
    private TripStatus tripStatus;

    public Trip(int id) {
        this.tripId = id;
    }
}
