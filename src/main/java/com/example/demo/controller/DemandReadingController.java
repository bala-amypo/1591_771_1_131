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

    @PostMapping("/")
    public ResponseEntity<DemandReading> createReading(@RequestBody DemandReading reading) {
        return ResponseEntity.ok(demandReadingService.createReading(reading));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<DemandReading>> getReadingsForZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(demandReadingService.getReadingsForZone(zoneId));
    }

    @GetMapping("/zone/{zoneId}/latest")
    public ResponseEntity<DemandReading> getLatestReading(@PathVariable Long zoneId) {
        return ResponseEntity.ok(demandReadingService.getLatestReading(zoneId));
    }

    @GetMapping("/zone/{zoneId}/recent")
    public ResponseEntity<List<DemandReading>> getRecentReadings(
            @PathVariable Long zoneId, 
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(demandReadingService.getRecentReadings(zoneId, limit));
    }
}