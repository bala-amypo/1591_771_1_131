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
        return ResponseEntity.ok(loadSheddingService.triggerLoadShedding(forecastId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoadSheddingEvent> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(loadSheddingService.getEventById(id));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<LoadSheddingEvent>> getEventsForZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(loadSheddingService.getEventsForZone(zoneId));
    }

    @GetMapping("/")
    public ResponseEntity<List<LoadSheddingEvent>> getAllEvents() {
        return ResponseEntity.ok(loadSheddingService.getAllEvents());
    }
}