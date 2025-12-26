package com.example.demo.controller;

import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.service.LoadSheddingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/load-shedding")
public class LoadSheddingController {
    
    private final LoadSheddingService loadSheddingService;
    
    public LoadSheddingController(LoadSheddingService loadSheddingService) {
        this.loadSheddingService = loadSheddingService;
    }

    @PostMapping("/trigger/{forecastId}")
    public ResponseEntity<LoadSheddingEvent> triggerLoadShedding(@PathVariable Long forecastId) {
        LoadSheddingEvent event = loadSheddingService.triggerLoadShedding(forecastId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoadSheddingEvent> getEvent(@PathVariable Long id) {
        LoadSheddingEvent event = loadSheddingService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<LoadSheddingEvent>> getEventsForZone(@PathVariable Long zoneId) {
        List<LoadSheddingEvent> events = loadSheddingService.getEventsForZone(zoneId);
        return ResponseEntity.ok(events);
    }

    @GetMapping
    public ResponseEntity<List<LoadSheddingEvent>> getAllEvents() {
        List<LoadSheddingEvent> events = loadSheddingService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}