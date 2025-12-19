package com.example.demo.controller;

import com.example.demo.entity.DemandReading;
import com.example.demo.service.DemandReadingService;
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
    public DemandReading createReading(@RequestBody DemandReading reading) {
        return demandReadingService.createReading(reading);
    }

    @GetMapping("/zone/{zoneId}")
    public List<DemandReading> getReadingsForZone(@PathVariable Long zoneId) {
        return demandReadingService.getReadingsForZone(zoneId);
    }

    @GetMapping("/zone/{zoneId}/latest")
    public DemandReading getLatestReading(@PathVariable Long zoneId) {
        return demandReadingService.getLatestReading(zoneId);
    }

    @GetMapping("/zone/{zoneId}/recent")
    public List<DemandReading> getRecentReadings(
            @PathVariable Long zoneId,
            @RequestParam int limit) {
        return demandReadingService.getRecentReadings(zoneId, limit);
    }
}
