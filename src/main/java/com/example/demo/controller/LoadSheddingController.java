package com.example.demo.controller;

import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.service.LoadSheddingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/load-shedding")
public class LoadSheddingController {

    private final LoadSheddingService service;

    public LoadSheddingController(LoadSheddingService service) {
        this.service = service;
    }

    @PostMapping("/trigger/{forecastId}")
    public LoadSheddingEvent trigger(@PathVariable Long forecastId) {
        return service.triggerLoadShedding(forecastId);
    }

    @GetMapping("/{id}")
    public LoadSheddingEvent get(@PathVariable Long id) {
        return service.getEventById(id);
    }

    @GetMapping("/zone/{zoneId}")
    public List<LoadSheddingEvent> zoneEvents(@PathVariable Long zoneId) {
        return service.getEventsForZone(zoneId);
    }

    @GetMapping
    public List<LoadSheddingEvent> all() {
        return service.getAllEvents();
    }
}
