package com.example.demo.controller;

import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.service.LoadSheddingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/load-shedding")
public class LoadSheddingController {

    private final LoadSheddingService loadSheddingService;

    public LoadSheddingController(
            LoadSheddingService loadSheddingService) {
        this.loadSheddingService = loadSheddingService;
    }

    @PostMapping("/trigger/{forecastId}")
    public LoadSheddingEvent triggerLoadShedding(
            @PathVariable Long forecastId) {
        return loadSheddingService.triggerLoadShedding(forecastId);
    }

    @GetMapping("/{id}")
    public LoadSheddingEvent getEventById(
            @PathVariable Long id) {
        return loadSheddingService.getEventById(id);
    }

    @GetMapping("/zone/{zoneId}")
    public List<LoadSheddingEvent> getEventsForZone(
            @PathVariable Long zoneId) {
        return loadSheddingService.getEventsForZone(zoneId);
    }

    @GetMapping
    public List<LoadSheddingEvent> getAllEvents() {
        return loadSheddingService.getAllEvents();
    }
}
