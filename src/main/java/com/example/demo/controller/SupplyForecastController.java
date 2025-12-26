package com.example.demo.controller;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.service.SupplyForecastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply-forecasts")
public class SupplyForecastController {
    
    private final SupplyForecastService supplyForecastService;
    
    public SupplyForecastController(SupplyForecastService supplyForecastService) {
        this.supplyForecastService = supplyForecastService;
    }

    @PostMapping
    public ResponseEntity<SupplyForecast> createForecast(@RequestBody SupplyForecast forecast) {
        SupplyForecast createdForecast = supplyForecastService.createForecast(forecast);
        return ResponseEntity.ok(createdForecast);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplyForecast> updateForecast(@PathVariable Long id, @RequestBody SupplyForecast forecast) {
        SupplyForecast updatedForecast = supplyForecastService.updateForecast(id, forecast);
        return ResponseEntity.ok(updatedForecast);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyForecast> getForecast(@PathVariable Long id) {
        SupplyForecast forecast = supplyForecastService.getForecastById(id);
        return ResponseEntity.ok(forecast);
    }

    @GetMapping("/latest")
    public ResponseEntity<SupplyForecast> getLatestForecast() {
        SupplyForecast forecast = supplyForecastService.getLatestForecast();
        return ResponseEntity.ok(forecast);
    }

    @GetMapping
    public ResponseEntity<List<SupplyForecast>> getAllForecasts() {
        List<SupplyForecast> forecasts = supplyForecastService.getAllForecasts();
        return ResponseEntity.ok(forecasts);
    }
}