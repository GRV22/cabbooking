package com.example.demo.cabbooking;

import com.example.demo.cabbooking.client.ClientPrinter;
import com.example.demo.cabbooking.server.CabBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CabBookingSystem implements CommandLineRunner {

    @Autowired
    private CabBooking cabBooking;
    @Autowired
    private ClientPrinter clientPrinter;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("********** Starting Cab Booking App ************");

        clientPrinter.printRiderRegistrationResponse(cabBooking.registerRider("Ganesh", "ganesh@gmail.com"));
        clientPrinter.printDriverRegistrationResponse(cabBooking.registerDriver("Deva", "deva@gmail.com"));
        clientPrinter.printCabRegistrationResponse(cabBooking.registerCab(1, "UXDYFD"));
        clientPrinter.printDriverRegistrationResponse(cabBooking.registerDriver("Yuva", "yuva@gmail.com"));
        clientPrinter.printCabRegistrationResponse(cabBooking.registerCab(2, "XDFSFD"));
        clientPrinter.printUpdateRiderLocationResponse(cabBooking.updateRiderLocation(1, 10.0, 10.0));
        clientPrinter.printUpdateCabLocationResponse(cabBooking.updateCabLocation(1, 10.0, 10.0));
        clientPrinter.printBookCabResponse(cabBooking.bookCab(1));
        clientPrinter.printUpdateCabLocationResponse(cabBooking.updateCabLocation(2, 100.0, 100.0));
        clientPrinter.printAllRideHistory(cabBooking.getRideHistory(1));

        cabBooking.printAllRiders();

    }
}
