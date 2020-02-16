package com.example.demo.cabbooking.contracts;

public interface IDriver {
    boolean updateCabLocation(Integer driverId, double x, double y);
    boolean updateAvailablity(Integer driverId, boolean available);
    int registerCab(Integer driverId, String vehicleNo);
}
