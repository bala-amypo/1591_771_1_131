package com.example.demo.controller;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.service.SupplyForecastService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply-forecasts")
public class SupplyForecastController {

    private final SupplyForecastService service;

    public SupplyForecastController(SupplyForecastService service) {
        this.service = service;
    }

    @PostMapping
    public SupplyForecast create(@RequestBody SupplyForecast f) {
        return service.createForecast(f);
    }

    @PutMapping("/{id}")
    public SupplyForecast update(@PathVariable Long id,
                                 @RequestBody SupplyForecast f) {
        return service.updateForecast(id, f);
    }

    @GetMapping("/{id}")
    public SupplyForecast get(@PathVariable Long id) {
        return service.getForecastById(id);
    }

    @GetMapping("/latest")
    public SupplyForecast latest() {
        return service.getLatestForecast();
    }

    @GetMapping
    public List<SupplyForecast> list() {
        return service.getAllForecasts();
    }
}
