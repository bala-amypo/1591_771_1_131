// SupplyForecastServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.SupplyForecast;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SupplyForecastRepository;
import com.example.demo.service.SupplyForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplyForecastServiceImpl implements SupplyForecastService {
    
    private final SupplyForecastRepository forecastRepository;
    
    @Override
    @Transactional
    public SupplyForecast createForecast(SupplyForecast forecast) {
        // Validate supply is non-negative
        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("Available supply must be >= 0");
        }
        
        // Validate time range
        if (forecast.getForecastStart().isAfter(forecast.getForecastEnd()) ||
            forecast.getForecastStart().equals(forecast.getForecastEnd())) {
            throw new BadRequestException("Forecast start must be before end (invalid range)");
        }
        
        forecast.setGeneratedAt(Instant.now());
        return forecastRepository.save(forecast);
    }
    
    @Override
    @Transactional
    public SupplyForecast updateForecast(Long id, SupplyForecast forecast) {
        SupplyForecast existing = forecastRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Forecast not found with id: " + id));
        
        if (forecast.getAvailableSupplyMW() != null) {
            existing.setAvailableSupplyMW(forecast.getAvailableSupplyMW());
        }
        if (forecast.getForecastStart() != null) {
            existing.setForecastStart(forecast.getForecastStart());
        }
        if (forecast.getForecastEnd() != null) {
            existing.setForecastEnd(forecast.getForecastEnd());
        }
        
        return forecastRepository.save(existing);
    }
    
    @Override
    public SupplyForecast getLatestForecast() {
        return forecastRepository.findFirstByOrderByGeneratedAtDesc()
            .orElseThrow(() -> new ResourceNotFoundException("No forecasts available"));
    }
    
    @Override
    public List<SupplyForecast> getAllForecasts() {
        return forecastRepository.findAll();
    }
}