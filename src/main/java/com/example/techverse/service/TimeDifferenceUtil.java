package com.example.techverse.service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeDifferenceUtil {

    public static String formatTimeDifference(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
       long min=seconds/60;
        
        long hours = seconds / 3600;
        long days = seconds / (3600 * 24);
        long weeks = days / 7;
        if(min<60) {
        	return min +" min";
        }
        if (hours < 24) {
            return hours + " hours";
        }

        
        if (days < 7) {
            return days + " days";
        }

      
        return weeks + " weeks";
    }

     
}

