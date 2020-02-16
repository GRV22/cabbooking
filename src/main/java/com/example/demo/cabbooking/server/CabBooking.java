package com.example.demo.cabbooking.server;

import com.example.demo.cabbooking.contracts.IRider;
import com.example.demo.cabbooking.contracts.GuestInterface;
import com.example.demo.cabbooking.contracts.IAdmin;
import com.example.demo.cabbooking.contracts.ICabBooking;
import com.example.demo.cabbooking.contracts.IDriver;
import com.example.demo.cabbooking.entities.Cab;
import com.example.demo.cabbooking.entities.Driver;
import com.example.demo.cabbooking.entities.Location;
import com.example.demo.cabbooking.entities.Rider;
import com.example.demo.cabbooking.entities.Trip;
import com.example.demo.cabbooking.enums.CabStatus;
import com.example.demo.cabbooking.enums.TripStatus;
import com.example.demo.cabbooking.utilities.CabUtil;
import com.example.demo.cabbooking.utilities.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@Scope("singleton")
public class CabBooking implements ICabBooking, GuestInterface, IDriver, IRider, IAdmin {

    private Map<Integer, Cab> allCabs;
    private Map<String, Integer> vehicleNoToCabIdMap;
    private ReadWriteLock cabLock = new ReentrantReadWriteLock();
    private Map<Integer, Rider> riders;
    private Map<String, Integer> emaildToRiderIdMap;
    private ReadWriteLock riderLock = new ReentrantReadWriteLock();
    private Map<Integer, Driver> drivers;
    private Map<String, Integer> emaildToDriverIdMap;
    private ReadWriteLock driverLock = new ReentrantReadWriteLock();
    private Map<Integer, Trip> trips;
    private double maxDistAllowed;

    @Autowired
    private KeyGenerator keyGenerator;
    @Autowired
    private CabUtil cabUtil;

    public CabBooking() {
        allCabs = new HashMap<>();
        riders = new HashMap<>();
        drivers = new HashMap<>();
        trips = new HashMap<>();
        emaildToRiderIdMap = new HashMap<>();
        emaildToDriverIdMap = new HashMap<>();
        vehicleNoToCabIdMap = new HashMap<>();
        maxDistAllowed = 100.0;
    }

    @Override
    public int registerRider(String name, String emaildId) {
        if (emaildToRiderIdMap.containsKey(emaildId)) {
            return 0;
        }
        int riderId = keyGenerator.getNextRiderId();
        Lock writeLock = riderLock.writeLock();
        try {
            writeLock.lock();
            final Rider rider = new Rider(riderId);
            rider.setName(name);
            rider.setEmailId(emaildId);
            riders.put(riderId, rider);
        } finally {
            writeLock.unlock();
        }
        return riderId;
    }

    @Override
    public int registerDriver(String name, String emailId) {
        if (emaildToDriverIdMap.containsKey(emailId)) {
            return 0;
        }
        int driverId = keyGenerator.getNextDriverId();
        Lock writeLock = driverLock.writeLock();
        try {
            writeLock.lock();
            final Driver driver = new Driver(driverId);
            driver.setName(name);
            driver.setEmailId(emailId);
            drivers.put(driverId, driver);
        } finally {
            writeLock.unlock();
        }
        return driverId;
    }


    @Override
    public boolean updateCabLocation(Integer driverId, double x, double y) {
        Driver driver = drivers.get(driverId);
        if (driver == null || driver.getAssignedCab() == null) {
            System.out.println("Either driver not exists or cab is not assigned to driver");
            return false;
        }
        Lock writeLock = cabLock.writeLock();
        try {
            writeLock.lock();
            driver.getAssignedCab().setLocation(new Location(x, y));
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean updateAvailablity(Integer driverId, boolean available) {
        Driver driver = drivers.get(driverId);
        if (driver == null) {
            return false;
        }
        Lock writeLock = driverLock.writeLock();
        Lock cabWriteLock = cabLock.writeLock();
        try {
            writeLock.lock();
            cabWriteLock.lock();
            return driver.changeAvailability(available);
        } finally {
            cabWriteLock.unlock();
            writeLock.unlock();
        }
    }

    @Override
    public int registerCab(Integer driverId, String vehicleNo) {
        if (vehicleNoToCabIdMap.containsKey(vehicleNo) || drivers.containsKey(driverId)) {
            return 0;
        }
        Lock driverWriteLock = driverLock.writeLock();
        Lock cabWriteLock = cabLock.writeLock();
        try {
            driverWriteLock.lock();
            cabWriteLock.lock();
            Driver driver = drivers.get(driverId);
            if (driver == null) {
                return 0;
            }
            int cabId = keyGenerator.getNextCabId();
            Cab cab = new Cab(cabId, vehicleNo);
            driver.setAssignedCab(cab);
            cab.setAssignedDriver(driver);
            allCabs.put(cabId, cab);
            return cabId;
        } finally {
            cabWriteLock.unlock();
            driverWriteLock.unlock();
        }
    }

    @Override
    public int bookCab(Integer riderId) {
        Rider rider = riders.get(riderId);
        if (null != rider && rider.getLastLocation() != null) {
            for (Cab cab : allCabs.values()) {
                if (CabStatus.AVAILABLE.equals(cab.getAvailablityStatus()) && cab.getLocation() != null) {
                    if (cabUtil.getDistance(cab.getLocation(), rider.getLastLocation()) < maxDistAllowed) {
                        cab.setAvailablityStatus(CabStatus.BUSY);
                        Trip trip = new Trip(keyGenerator.getNextTripId());
                        trip.setRiderId(riderId);
                        trip.setDriverId(cab.getAssignedDriver().getDriverId());
                        trip.setStartLocation(new Location(rider.getLastLocation()));
                        trip.setTripStatus(TripStatus.START);
                        trips.put(trip.getTripId(), trip);
                        return trip.getTripId();
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public boolean updateRiderLocation(Integer riderId, double x, double y) {
        final Rider rider = riders.get(riderId);
        if (rider == null) {
            System.out.println("Rider doesn't exist");
            return false;
        }
        Lock writeLock = riderLock.writeLock();
        try {
            writeLock.lock();
            rider.setLastLocation(new Location(x, y));
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public List<String> getRideHistory(Integer riderId) {
        List<String> allTripByRider = new ArrayList<>();
        for (Trip trip : trips.values()) {
            if (riderId == trip.getRiderId()) {
                allTripByRider.add(trip.toString());
            }
        }
        return allTripByRider;
    }

    @Override
    public boolean endTrip(Integer riderId, double x, double y) {

        return false;
    }

    @Override
    public void printAllRiders() {
        for (Rider rider : riders.values()) {
            System.out.println(rider);
        }
    }

    @Override
    public void printAllCabs() {
        for (Cab cab : allCabs.values()) {
            System.out.println(cab);
        }
    }

    @Override
    public void printAllTrips() {
        for (Trip trip : trips.values()) {
            System.out.println(trip);
        }
    }

    @Override
    public void printAllDrivers() {
        for (Driver driver : drivers.values()) {
            System.out.println(driver);
        }
    }
}
