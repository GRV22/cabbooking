package com.example.demo.cabbooking.utilities;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("singleton")
public class KeyGenerator {
    private AtomicInteger nextCabId = new AtomicInteger(1);
    private AtomicInteger nextDriverId = new AtomicInteger(1);
    private AtomicInteger nextRiderId = new AtomicInteger(1);
    private AtomicInteger nextTripId = new AtomicInteger(1);


    public int getNextCabId() {
        return nextCabId.getAndIncrement();
    }

    public int getNextDriverId() {
        return nextDriverId.getAndIncrement();
    }

    public int getNextRiderId() {
        return nextRiderId.getAndIncrement();
    }

    public int getNextTripId() {
        return nextTripId.getAndIncrement();
    }

}
