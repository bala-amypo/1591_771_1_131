package com.example.demo.controller;

import com.example.demo.entity.DemandReading;
import com.example.demo.service.DemandReadingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demand-readings")
public class DemandReadingController {
    
    private final DemandReadingService demandReadingService;
    
    public DemandReadingController(DemandReadingService demandReadingService) {
        this.demandReadingService = demandReadingService;
    }

    @PostMapping
    public ResponseEntity<DemandReading> createReading(@RequestBody DemandReading reading) {
        DemandReading createdReading = demandReadingService.createReading(reading);
        return ResponseEntity.ok(createdReading);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<DemandReading>> getReadingsForZone(@PathVariable Long zoneId) {
        List<DemandReading> readings = demandReadingService.getReadingsForZone(zoneId);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/zone/{zoneId}/latest")
    public ResponseEntity<DemandReading> getLatestReading(@PathVariable Long zoneId) {
        DemandReading reading = demandReadingService.getLatestReading(zoneId);
        return ResponseEntity.ok(reading);
    }

    @GetMapping("/zone/{zoneId}/recent")
    public ResponseEntity<List<DemandReading>> getRecentReadings(@PathVariable Long zoneId, @RequestParam int limit) {
        List<DemandReading> readings = demandReadingService.getRecentReadings(zoneId, limit);
        return ResponseEntity.ok(readings);
    }
}