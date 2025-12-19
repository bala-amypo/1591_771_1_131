package com.example.demo.controller;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.service.SupplyForecastService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply-forecasts")
public class SupplyForecastController {

    private final SupplyForecastService supplyForecastService;

    public SupplyForecastController(
            SupplyForecastService supplyForecastService) {
        this.supplyForecastService = supplyForecastService;
    }

    @PostMapping
    public SupplyForecast createForecast(
            @RequestBody SupplyForecast forecast) {
        return supplyForecastService.createForecast(forecast);
    }

    @PutMapping("/{id}")
    public SupplyForecast updateForecast(
            @PathVariable Long id,
            @RequestBody SupplyForecast forecast) {
        return supplyForecastService.updateForecast(id, forecast);
    }

    @GetMapping("/{id}")
    public SupplyForecast getForecastById(
            @PathVariable Long id) {
        return supplyForecastService.getForecastById(id);
    }

    @GetMapping("/latest")
    public SupplyForecast getLatestForecast() {
        return supplyForecastService.getLatestForecast();
    }

    @GetMapping
    public List<SupplyForecast> getAllForecasts() {
        return supplyForecastService.getAllForecasts();
    }
}
