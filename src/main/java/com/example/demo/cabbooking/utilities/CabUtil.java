package com.example.demo.cabbooking.utilities;

import com.example.demo.cabbooking.entities.Location;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CabUtil {
    public double getDistance(Location a, Location b) {
        return Math.sqrt(Math.pow(Math.abs(a.x - b.x), 2) + Math.pow(Math.abs(a.y - b.y), 2));
    }
}
