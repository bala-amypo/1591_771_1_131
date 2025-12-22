package com.example.demo.controller;

import com.example.demo.entity.DemandReading;
import com.example.demo.service.DemandReadingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demand-readings")
public class DemandReadingController {

    private final DemandReadingService service;

    public DemandReadingController(DemandReadingService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<DemandReading> create(@RequestBody DemandReading reading) {
        return ResponseEntity.ok(service.createReading(reading));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<DemandReading>> getByZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(service.getReadingsForZone(zoneId));
    }

    @GetMapping("/zone/{zoneId}/latest")
    public ResponseEntity<DemandReading> getLatest(@PathVariable Long zoneId) {
        return ResponseEntity.ok(service.getLatestReading(zoneId));
    }

    @GetMapping("/zone/{zoneId}/recent")
    public ResponseEntity<List<DemandReading>> getRecent(
            @PathVariable Long zoneId, 
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(service.getRecentReadings(zoneId, limit));
    }
}