// LoadSheddingServiceImpl.java
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoadSheddingServiceImpl implements LoadSheddingService {
    
    private final SupplyForecastRepository forecastRepository;
    private final ZoneRepository zoneRepository;
    private final DemandReadingRepository readingRepository;
    private final LoadSheddingEventRepository eventRepository;
    
    @Override
    @Transactional
    public LoadSheddingEvent triggerLoadShedding(Long forecastId) {
        // Get forecast
        SupplyForecast forecast = forecastRepository.findById(forecastId)
            .orElseThrow(() -> new ResourceNotFoundException("Forecast not found with id: " + forecastId));
        
        // Get active zones ordered by priority (lowest priority first for shedding)
        List<Zone> activeZones = zoneRepository.findByActiveTrueOrderByPriorityLevelAsc();
        
        if (activeZones.isEmpty()) {
            throw new BadRequestException("No overload detected or No suitable zones available");
        }
        
        // Calculate total demand
        double totalDemand = 0.0;
        for (Zone zone : activeZones) {
            Optional<DemandReading> latestReading = 
                readingRepository.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId());
            if (latestReading.isPresent()) {
                totalDemand += latestReading.get().getDemandMW();
            }
        }
        
        // Check if there's actually an overload
        double availableSupply = forecast.getAvailableSupplyMW();
        if (totalDemand <= availableSupply) {
            throw new BadRequestException("No overload detected");
        }
        
        // Calculate deficit
        double deficit = totalDemand - availableSupply;
        
        // Select zone for shedding (start from lowest priority)
        Zone selectedZone = null;
        double zoneDemand = 0.0;
        
        // Start from the end of the list (lowest priority comes first in ascending order)
        for (int i = activeZones.size() - 1; i >= 0; i--) {
            Zone zone = activeZones.get(i);
            Optional<DemandReading> reading = 
                readingRepository.findFirstByZoneIdOrderByRecordedAtDesc(zone.getId());
            
            if (reading.isPresent() && reading.get().getDemandMW() > 0) {
                selectedZone = zone;
                zoneDemand = reading.get().getDemandMW();
                break;
            }
        }
        
        if (selectedZone == null) {
            throw new BadRequestException("No suitable zones available for load shedding");
        }
        
        // Create event
        LoadSheddingEvent event = LoadSheddingEvent.builder()
            .zone(selectedZone)
            .eventStart(Instant.now())
            .reason("Supply shortage - deficit: " + deficit + " MW")
            .expectedDemandReductionMW(zoneDemand)
            .createdAt(Instant.now())
            .build();
        
        return eventRepository.save(event);
    }
    
    @Override
    public LoadSheddingEvent getEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }
    
    @Override
    public List<LoadSheddingEvent> getAllEvents() {
        return eventRepository.findAll();
    }
    
    @Override
    public List<LoadSheddingEvent> getEventsForZone(Long zoneId) {
        return eventRepository.findByZoneIdOrderByEventStartDesc(zoneId);
    }
}