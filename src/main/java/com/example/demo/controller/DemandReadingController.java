package com.example.demo.controller;

import com.example.demo.entity.DemandReading;
import com.example.demo.service.DemandReadingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demand-readings")
public class DemandReadingController {

    private final DemandReadingService service;

    public DemandReadingController(DemandReadingService service) {
        this.service = service;
    }

    @PostMapping
    public DemandReading create(@RequestBody DemandReading reading) {
        return service.createReading(reading);
    }

    @GetMapping("/zone/{zoneId}")
    public List<DemandReading> list(@PathVariable Long zoneId) {
        return service.getReadingsForZone(zoneId);
    }

    @GetMapping("/zone/{zoneId}/latest")
    public DemandReading latest(@PathVariable Long zoneId) {
        return service.getLatestReading(zoneId);
    }

    @GetMapping("/zone/{zoneId}/recent")
    public List<DemandReading> recent(@PathVariable Long zoneId,
                                      @RequestParam int limit) {
        return service.getRecentReadings(zoneId, limit);
    }
}
