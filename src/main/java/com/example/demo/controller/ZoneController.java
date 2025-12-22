package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneServiceImpl zoneService;

    @PostMapping("/") public Zone create(@RequestBody Zone zone) { return zoneService.createZone(zone); }
    @PutMapping("/{id}") public Zone update(@PathVariable Long id, @RequestBody Zone zone) { return zoneService.updateZone(id, zone); }
    @PutMapping("/{id}/deactivate") public void deactivate(@PathVariable Long id) { zoneService.deactivateZone(id); }
}

@RestController
@RequestMapping("/api/load-shedding")
@RequiredArgsConstructor
public class LoadSheddingController {
    private final LoadSheddingServiceImpl loadSheddingService;

    @PostMapping("/trigger/{forecastId}")
    public LoadSheddingEvent trigger(@PathVariable Long forecastId) {
        return loadSheddingService.triggerLoadShedding(forecastId);
    }
}