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

    private final SupplyForecastRepository supplyForecastRepository;

    // Constructor injection (ONLY repository)
    public SupplyForecastServiceImpl(
            SupplyForecastRepository supplyForecastRepository) {
        this.supplyForecastRepository = supplyForecastRepository;
    }

    @Override
    public SupplyForecast createForecast(SupplyForecast forecast) {

        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("supply must be >= 0");
        }

        if (forecast.getForecastStart()
                .isAfter(forecast.getForecastEnd())) {
            throw new BadRequestException("invalid forecast range");
        }

        return supplyForecastRepository.save(forecast);
    }

    @Override
    public SupplyForecast updateForecast(Long id, SupplyForecast forecast) {

        SupplyForecast existing =
                supplyForecastRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Forecast not found"));

        if (forecast.getAvailableSupplyMW() < 0) {
            throw new BadRequestException("supply must be >= 0");
        }

        if (forecast.getForecastStart()
                .isAfter(forecast.getForecastEnd())) {
            throw new BadRequestException("invalid forecast range");
        }

        existing.setAvailableSupplyMW(forecast.getAvailableSupplyMW());
        existing.setForecastStart(forecast.getForecastStart());
        existing.setForecastEnd(forecast.getForecastEnd());

        return supplyForecastRepository.save(existing);
    }

    @Override
    public SupplyForecast getForecastById(Long id) {
        return supplyForecastRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Forecast not found"));
    }

    @Override
    public SupplyForecast getLatestForecast() {
        return supplyForecastRepository
                .findFirstByOrderByGeneratedAtDesc()
                .orElseThrow(() ->
                        new ResourceNotFoundException("No forecasts"));
    }

    @Override
    public List<SupplyForecast> getAllForecasts() {
        return supplyForecastRepository.findAll();
    }
}
