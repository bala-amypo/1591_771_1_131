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

    /**
     * Constructor must accept repositories in this exact order: 
     * Forecast, Zone, DemandReading, Event.
     */
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
        // 1. Fetch forecast or throw 404
        SupplyForecast forecast = forecastRepo.findById(forecastId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));

        // 2. Calculate current total demand from latest readings of all active zones
        List<Zone> activeZones = zoneRepo.findByActiveTrueOrderByPriorityLevelAsc();
        double totalDemand = 0.0;

        for (Zone zone : activeZones) {
            totalDemand += readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId())
                    .map(DemandReading::getDemandMW)
                    .orElse(0.0);
        }

        // 3. Rule: If supply >= demand, no shedding needed
        if (totalDemand <= forecast.getAvailableSupplyMW()) {
            throw new BadRequestException("No overload");
        }

        // 4. Identify candidate zones; if none active, throw 400
        if (activeZones.isEmpty()) {
            throw new BadRequestException("No suitable candidate zones");
        }

        // 5. Select the zone with the highest priorityLevel value (lowest actual priority)
        Zone lowestPriorityZone = activeZones.get(activeZones.size() - 1);
        
        double shedAmount = readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(lowestPriorityZone.getId())
                .map(DemandReading::getDemandMW)
                .orElse(0.0);

        // 6. Create and save the event
        LoadSheddingEvent event = LoadSheddingEvent.builder()
                .zone(lowestPriorityZone)
                .eventStart(Instant.now())
                .reason("Grid capacity exceeded")
                .triggeredByForecastId(forecastId)
                .expectedDemandReductionMW(shedAmount)
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