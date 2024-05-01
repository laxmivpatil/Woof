package com.example.techverse;

import java.lang.Math;

public class DistanceCalculator {
    private static final double EARTH_RADIUS_KM = 6371.0; // Earth's radius in kilometers

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the differences in coordinates
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine formula to calculate distance
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        return distance;
    }

    public static void main(String[] args) {
        // Example coordinates for two points
        double lat1 = 52.5200;
        double lon1 = 13.4050;
        double lat2 = 48.8566;
        double lon2 = 2.3522;

        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        System.out.println("Distance between the points: " + distance + " kilometers");
    }
}
