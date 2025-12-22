package com.example.demo.controller;

import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.service.LoadSheddingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/load-shedding")
@CrossOrigin("*") // Fixes CORS "Failed to fetch" in Swagger
public class LoadSheddingController {

    private final LoadSheddingService loadSheddingService;

    public LoadSheddingController(LoadSheddingService loadSheddingService) {
        this.loadSheddingService = loadSheddingService;
    }

    @PostMapping("/trigger/{forecastId}")
    public LoadSheddingEvent trigger(@PathVariable Long forecastId) {
        // Delegates logic to service and returns created event
        return loadSheddingService.triggerLoadShedding(forecastId);
    }

    @GetMapping("/{id}")
    public LoadSheddingEvent getById(@PathVariable Long id) {
        // missing events must throw ResourceNotFoundException with "Event not found"
        return loadSheddingService.getEventById(id);
    }

    @GetMapping("/zone/{zoneId}")
    public List<LoadSheddingEvent> getByZone(@PathVariable Long zoneId) {
        // Returns list of events for a specific zone
        return loadSheddingService.getEventsForZone(zoneId);
    }

    @GetMapping("/")
    public List<LoadSheddingEvent> getAll() {
        // Simply returns all events
        return loadSheddingService.getAllEvents();
    }
}