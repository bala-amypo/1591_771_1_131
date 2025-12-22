package com.example.demo.service.impl;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SupplyForecastRepository;
import com.example.demo.service.SupplyForecastService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplyForecastServiceImpl implements SupplyForecastService {
    private final SupplyForecastRepository forecastRepo;

    // Constructor injection as required by tests
    public SupplyForecastServiceImpl(SupplyForecastRepository forecastRepo) {
        this.forecastRepo = forecastRepo;
    }

    @Override
    public SupplyForecast createForecast(SupplyForecast forecast) {
        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("availableSupplyMW must be >= 0");
        }
        if (forecast.getForecastStart().isAfter(forecast.getForecastEnd())) {
            throw new BadRequestException("Invalid range: forecastStart must be before forecastEnd");
        }
        return forecastRepo.save(forecast);
    }

    @Override
    public SupplyForecast getLatestForecast() {
        return forecastRepo.findFirstByOrderByGeneratedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("No forecasts"));
    }

    @Override
    public List<SupplyForecast> getAllForecasts() {
        return forecastRepo.findAll();
    }
    
    // Additional methods for update and getById omitted for brevity but follow same pattern
}