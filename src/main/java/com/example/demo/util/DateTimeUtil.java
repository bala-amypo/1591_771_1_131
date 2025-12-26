package com.example.demo.util;

import java.time.Instant;

public class DateTimeUtil {
    public static Instant now() {
        return Instant.now();
    }
    
    public static boolean isFuture(Instant instant) {
        return instant.isAfter(Instant.now());
    }
}