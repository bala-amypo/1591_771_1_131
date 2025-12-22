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
        validateSupplyForecast(forecast);
        return forecastRepo.save(forecast);
    }

    @Override
    public SupplyForecast updateForecast(Long id, SupplyForecast forecast) {
        if (!forecastRepo.existsById(id)) {
            throw new ResourceNotFoundException("Forecast not found"); //
        }
        validateSupplyForecast(forecast);
        forecast.setId(id);
        return forecastRepo.save(forecast);
    }

    @Override
    public SupplyForecast getForecastById(Long id) {
        return forecastRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found")); //
    }

    @Override
    public SupplyForecast getLatestForecast() {
        return forecastRepo.findFirstByOrderByGeneratedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("No forecasts")); //
    }

    @Override
    public List<SupplyForecast> getAllForecasts() {
        return forecastRepo.findAll();
    }

    private void validateSupplyForecast(SupplyForecast forecast) {
        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("availableSupplyMW must be >= 0"); //
        }
        if (forecast.getForecastStart().isAfter(forecast.getForecastEnd())) {
            throw new BadRequestException("Invalid range: forecastStart must be before forecastEnd"); //
        }
    }
}