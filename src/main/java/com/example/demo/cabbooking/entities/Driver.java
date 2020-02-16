package com.example.demo.cabbooking.entities;

import com.example.demo.cabbooking.enums.CabStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Driver extends Person {
    private int driverId;
    @Setter
    private Cab assignedCab;
    private boolean isAvailable;

    public Driver(int id) {
        this.driverId = id;
        isAvailable = true;
    }

    public boolean changeAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
        if (!isAvailable && assignedCab != null) {
            this.assignedCab.setAvailablityStatus(CabStatus.NOT_AVAILABE);
        }
        return true;
    }
}
