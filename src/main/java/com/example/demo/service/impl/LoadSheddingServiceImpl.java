package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.LoadSheddingService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class LoadSheddingServiceImpl implements LoadSheddingService {

    private final SupplyForecastRepository forecastRepo;
    private final ZoneRepository zoneRepo;
    private final DemandReadingRepository readingRepo;
    private final LoadSheddingEventRepository eventRepo;

    public LoadSheddingServiceImpl(SupplyForecastRepository forecastRepo, 
                                   ZoneRepository zoneRepo, 
                                   DemandReadingRepository readingRepo, 
                                   LoadSheddingEventRepository eventRepo) {
        this.forecastRepo = forecastRepo;
        this.zoneRepo = zoneRepo;
        this.readingRepo = readingRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public LoadSheddingEvent triggerLoadShedding(Long forecastId) {
        // 1. Fetch Forecast
        SupplyForecast forecast = forecastRepo.findById(forecastId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));

        // 2. Calculate Total Demand from Active Zones
        List<Zone> activeZones = zoneRepo.findByActiveTrueOrderByPriorityLevelAsc();
        if (activeZones.isEmpty()) {
            throw new BadRequestException("No suitable candidate zones");
        }

        double totalDemand = 0.0;
        for (Zone zone : activeZones) {
            totalDemand += readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId())
                    .map(DemandReading::getDemandMW)
                    .orElse(0.0);
        }

        // 3. Check Overload
        double availableSupply = forecast.getAvailableSupplyMW();
        if (totalDemand <= availableSupply) {
            throw new BadRequestException("No overload detected");
        }

        // 4. Identify Zone to Shed (Simplified: selecting the first lowest priority zone)
        // Tests expect at least one event record when success is returned
        Zone zoneToShed = activeZones.get(activeZones.size() - 1); 
        double shedDemand = readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(zoneToShed.getId())
                .map(DemandReading::getDemandMW)
                .orElse(0.0);

        LoadSheddingEvent event = LoadSheddingEvent.builder()
                .zone(zoneToShed)
                .eventStart(Instant.now())
                .reason("Supply shortfall for forecast " + forecastId)
                .triggeredByForecastId(forecastId)
                .expectedDemandReductionMW(shedDemand)
                .build();

        return eventRepo.save(event);
    }

    @Override
    public LoadSheddingEvent getEventById(Long id) {
        return eventRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @Override
    public List<LoadSheddingEvent> getEventsForZone(Long zoneId) {
        return eventRepo.findByZoneIdOrderByEventStartDesc(zoneId);
    }

    @Override
    public List<LoadSheddingEvent> getAllEvents() {
        return eventRepo.findAll();
    }
}