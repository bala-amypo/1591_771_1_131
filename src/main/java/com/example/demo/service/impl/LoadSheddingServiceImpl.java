package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.*;
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

    // Constructor order: forecast, zone, reading, event
    public LoadSheddingServiceImpl(SupplyForecastRepository forecastRepo, ZoneRepository zoneRepo, 
                                   DemandReadingRepository readingRepo, LoadSheddingEventRepository eventRepo) {
        this.forecastRepo = forecastRepo;
        this.zoneRepo = zoneRepo;
        this.readingRepo = readingRepo;
        this.eventRepo = eventRepo;
    }

    @Override
    public LoadSheddingEvent triggerLoadShedding(Long forecastId) {
        SupplyForecast forecast = forecastRepo.findById(forecastId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found")); //

        List<Zone> activeZones = zoneRepo.findByActiveTrueOrderByPriorityLevelAsc();
        double totalDemand = 0;

        for (Zone z : activeZones) {
            totalDemand += readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(z.getId())
                    .map(DemandReading::getDemandMW).orElse(0.0);
        }

        // Throw BadRequest if no overload exists
        if (totalDemand <= forecast.getAvailableSupplyMW()) {
            throw new BadRequestException("No overload");
        }

        if (activeZones.isEmpty()) {
            throw new BadRequestException("No suitable candidate zones"); //
        }

        // Select lowest-priority zone (last in list)
        Zone targetZone = activeZones.get(activeZones.size() - 1);
        double demandToShed = readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(targetZone.getId())
                .map(DemandReading::getDemandMW).orElse(0.0);

        LoadSheddingEvent event = LoadSheddingEvent.builder()
                .zone(targetZone)
                .eventStart(Instant.now())
                .reason("Grid Overload")
                .triggeredByForecastId(forecastId)
                .expectedDemandReductionMW(demandToShed)
                .build();

        return eventRepo.save(event); // Successful save makes data visible in SQL
    }

    @Override
    public LoadSheddingEvent getEventById(Long id) {
        return eventRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found")); //
    }

    @Override
    public List<LoadSheddingEvent> getEventsForZone(Long zoneId) {
        return eventRepo.findByZoneIdOrderByEventStartDesc(zoneId); //
    }

    @Override
    public List<LoadSheddingEvent> getAllEvents() {
        return eventRepo.findAll(); //
    }
}