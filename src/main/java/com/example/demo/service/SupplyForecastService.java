package com.example.demo.service;

import com.example.demo.entity.SupplyForecast;

import java.util.List;

public interface SupplyForecastService {

    SupplyForecast createForecast(SupplyForecast forecast);

    SupplyForecast updateForecast(Long id, SupplyForecast forecast);

    SupplyForecast getForecastById(Long id);

    SupplyForecast getLatestForecast();

    List<SupplyForecast> getAllForecasts();
}
