package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.entity.SupplyForecast;
import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.LoadSheddingEventRepository;
import com.example.demo.repository.SupplyForecastRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.LoadSheddingService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class LoadSheddingServiceImpl implements LoadSheddingService {
    
    private final SupplyForecastRepository supplyForecastRepository;
    private final ZoneRepository zoneRepository;
    private final DemandReadingRepository demandReadingRepository;
    private final LoadSheddingEventRepository loadSheddingEventRepository;
    
    public LoadSheddingServiceImpl(SupplyForecastRepository supplyForecastRepository,
                                   ZoneRepository zoneRepository,
                                   DemandReadingRepository demandReadingRepository,
                                   LoadSheddingEventRepository loadSheddingEventRepository) {
        this.supplyForecastRepository = supplyForecastRepository;
        this.zoneRepository = zoneRepository;
        this.demandReadingRepository = demandReadingRepository;
        this.loadSheddingEventRepository = loadSheddingEventRepository;
    }

    @Override
    public LoadSheddingEvent triggerLoadShedding(Long forecastId) {
        SupplyForecast forecast = supplyForecastRepository.findById(forecastId)
                .orElseThrow(() -> new ResourceNotFoundException("Forecast not found"));
        
        List<Zone> activeZones = zoneRepository.findByActiveTrueOrderByPriorityLevelAsc();
        
        double totalDemand = 0;
        for (Zone zone : activeZones) {
            Optional<DemandReading> latestReading = demandReadingRepository.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId());
            if (latestReading.isPresent()) {
                totalDemand += latestReading.get().getDemandMW();
            }
        }
        
        double availableSupply = forecast.getAvailableSupplyMW();
        
        if (totalDemand <= availableSupply) {
            throw new BadRequestException("No overload detected");
        }
        
        double deficit = totalDemand - availableSupply;
        
        // Find zones to shed - prioritize lowest priority (highest priority number)
        Zone targetZone = null;
        double targetDemand = 0;
        
        for (int i = activeZones.size() - 1; i >= 0; i--) {
            Zone zone = activeZones.get(i);
            Optional<DemandReading> latestReading = demandReadingRepository.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId());
            if (latestReading.isPresent() && latestReading.get().getDemandMW() > 0) {
                targetZone = zone;
                targetDemand = latestReading.get().getDemandMW();
                break;
            }
        }
        
        if (targetZone == null) {
            throw new BadRequestException("No suitable zones for load shedding");
        }
        
        LoadSheddingEvent event = LoadSheddingEvent.builder()
                .zone(targetZone)
                .eventStart(Instant.now())
                .reason("Supply deficit: " + deficit + " MW")
                .triggeredByForecastId(forecastId)
                .expectedDemandReductionMW(targetDemand)
                .build();
        
        return loadSheddingEventRepository.save(event);
    }

    @Override
    public LoadSheddingEvent getEventById(Long id) {
        return loadSheddingEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    @Override
    public List<LoadSheddingEvent> getEventsForZone(Long zoneId) {
        return loadSheddingEventRepository.findByZoneIdOrderByEventStartDesc(zoneId);
    }

    @Override
    public List<LoadSheddingEvent> getAllEvents() {
        return loadSheddingEventRepository.findAll();
    }
}