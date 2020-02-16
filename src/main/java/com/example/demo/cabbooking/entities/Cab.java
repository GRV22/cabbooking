package com.example.demo.cabbooking.entities;

import com.example.demo.cabbooking.enums.CabStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Cab {
    private int cabId;
    private String vehicleNo;
    @Setter
    private CabStatus availablityStatus;
    @Setter
    private Location location;
    @Setter
    private Driver assignedDriver;

    public Cab(int id, String vehicleNo) {
        cabId = id;
        this.vehicleNo = vehicleNo;
        this.availablityStatus = CabStatus.AVAILABLE;
    }
}
