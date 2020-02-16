package com.example.demo.cabbooking.client;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("singleton")
public class ClientPrinter {

    public void printRiderRegistrationResponse(int response) {
        if (response == 0) {
            System.out.println("Unable to register rider");
            return;
        }
        System.out.println("New rider is registered with riderId : " + response);
    }

    public void printDriverRegistrationResponse(int response) {
        if (response == 0) {
            System.out.println("Unable to register driver");
            return;
        }
        System.out.println("New driver is registered with driverId : " + response);
    }

    public void printCabRegistrationResponse(int response) {
        if (response == 0) {
            System.out.println("Unable to register cab");
            return;
        }
        System.out.println("New cab is registered with cabId : " + response);
    }

    public void printUpdateRiderLocationResponse(boolean response) {
        if (response) {
            System.out.println("Rider location is updated");
        } else {
            System.out.println("Unable to update Rider location");
        }
    }

    public void printUpdateCabLocationResponse(boolean response) {
        if (response) {
            System.out.println("Cab location is updated");
        } else {
            System.out.println("Unable to update Cab location");
        }
    }

    public void printBookCabResponse(int response) {
        if (response == 0) {
            System.out.println("Unable to book cab");
            return;
        }
        System.out.println("Cab is booked with tripId : " + response);
    }

    public void printAllRideHistory(final List<String> allTrips) {
        System.out.println("All ride history requested by rider");
        System.out.println(allTrips);
    }



}
