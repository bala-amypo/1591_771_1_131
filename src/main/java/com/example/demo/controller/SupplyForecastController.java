package com.example.demo.controller;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.service.SupplyForecastService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/supply-forecasts")
@CrossOrigin("*") // Allows Swagger to connect directly to this controller
public class SupplyForecastController {
    private final SupplyForecastService forecastService;

    public SupplyForecastController(SupplyForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @PostMapping("/")
    public SupplyForecast create(@RequestBody SupplyForecast forecast) {
        return forecastService.createForecast(forecast); //
    }

    @PutMapping("/{id}")
    public SupplyForecast update(@PathVariable Long id, @RequestBody SupplyForecast forecast) {
        return forecastService.updateForecast(id, forecast); //
    }

    @GetMapping("/{id}")
    public SupplyForecast getById(@PathVariable Long id) {
        return forecastService.getForecastById(id); //
    }

    @GetMapping("/latest")
    public SupplyForecast getLatest() {
        return forecastService.getLatestForecast(); //
    }

    @GetMapping("/")
    public List<SupplyForecast> getAll() {
        return forecastService.getAllForecasts(); //
    }
}