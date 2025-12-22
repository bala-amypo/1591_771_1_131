package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl {
    private final ZoneRepository zoneRepo;

    public Zone createZone(Zone zone) {
        if (zone.getPriorityLevel() < 1) throw new BadRequestException("Priority must be >= 1");
        if (zoneRepo.findByZoneName(zone.getZoneName()).isPresent()) throw new BadRequestException("Name must be unique");
        return zoneRepo.save(zone);
    }

    public Zone updateZone(Long id, Zone zoneDetails) {
        Zone zone = zoneRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        zone.setZoneName(zoneDetails.getZoneName());
        zone.setPriorityLevel(zoneDetails.getPriorityLevel());
        return zoneRepo.save(zone);
    }

    public void deactivateZone(Long id) {
        Zone zone = zoneRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        zone.setActive(false);
        zoneRepo.save(zone);
    }
}

@Service
@RequiredArgsConstructor
public class LoadSheddingServiceImpl {
    private final SupplyForecastRepository forecastRepo;
    private final ZoneRepository zoneRepo;
    private final DemandReadingRepository readingRepo;
    private final LoadSheddingEventRepository eventRepo;

    public LoadSheddingEvent triggerLoadShedding(Long forecastId) {
        SupplyForecast forecast = forecastRepo.findById(forecastId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));

        List<Zone> activeZones = zoneRepo.findByActiveTrueOrderByPriorityLevelAsc();
        double totalDemand = activeZones.stream()
                .mapToDouble(z -> readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(z.getId())
                        .map(DemandReading::getDemandMW).orElse(0.0)).sum();

        if (totalDemand <= forecast.getAvailableSupplyMW()) throw new BadRequestException("No overload");

        // Logic to pick the lowest priority zone (highest numeric priorityLevel)
        Zone target = activeZones.get(activeZones.size() - 1); 
        
        LoadSheddingEvent event = LoadSheddingEvent.builder()
                .zone(target).eventStart(Instant.now()).reason("Supply Shortage")
                .triggeredByForecastId(forecastId).expectedDemandReductionMW(totalDemand - forecast.getAvailableSupplyMW())
                .build();
        return eventRepo.save(event);
    }
}