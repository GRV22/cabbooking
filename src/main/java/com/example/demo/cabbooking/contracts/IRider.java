package com.example.demo.cabbooking.contracts;

import java.util.List;

public interface IRider {
    int bookCab(Integer riderId);
    boolean updateRiderLocation(Integer riderId, double x, double y);
    List<String> getRideHistory(Integer riderId);
    boolean endTrip(Integer riderId, double x, double y);
}
