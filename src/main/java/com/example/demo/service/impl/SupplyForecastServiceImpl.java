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

    private final SupplyForecastRepository forecastRepository;

    public SupplyForecastServiceImpl(SupplyForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    private void validateForecast(SupplyForecast forecast) {
        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("availableSupplyMW must be >= 0");
        }
        if (forecast.getForecastStart() != null && forecast.getForecastEnd() != null) {
            if (!forecast.getForecastStart().isBefore(forecast.getForecastEnd())) {
                throw new BadRequestException("Invalid time range: forecastStart must be before forecastEnd");
            }
        }
    }

    @Override
    public SupplyForecast createForecast(SupplyForecast forecast) {
        validateForecast(forecast);
        return forecastRepository.save(forecast);
    }

    @Override
    public SupplyForecast updateForecast(Long id, SupplyForecast forecastDetails) {
        SupplyForecast existing = forecastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));
        
        validateForecast(forecastDetails);
        
        existing.setAvailableSupplyMW(forecastDetails.getAvailableSupplyMW());
        existing.setForecastStart(forecastDetails.getForecastStart());
        existing.setForecastEnd(forecastDetails.getForecastEnd());
        
        return forecastRepository.save(existing);
    }

    @Override
    public SupplyForecast getForecastById(Long id) {
        return forecastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));
    }

    @Override
    public SupplyForecast getLatestForecast() {
        return forecastRepository.findFirstByOrderByGeneratedAtDesc()
                .orElseThrow(() -> new ResourceNotFoundException("No forecasts"));
    }

    @Override
    public List<SupplyForecast> getAllForecasts() {
        return forecastRepository.findAll();
    }
}