// DemandReadingServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.DemandReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DemandReadingServiceImpl implements DemandReadingService {
    
    private final DemandReadingRepository readingRepository;
    private final ZoneRepository zoneRepository;
    
    @Override
    @Transactional
    public DemandReading createReading(DemandReading reading) {
        // Validate zone exists
        Long zoneId = reading.getZone().getId();
        Zone zone = zoneRepository.findById(zoneId)
            .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + zoneId));
        
        // Validate timestamp is not in future
        if (reading.getRecordedAt().isAfter(Instant.now())) {
            throw new BadRequestException("Recorded timestamp cannot be in the future");
        }
        
        // Validate demand is non-negative
        if (reading.getDemandMW() < 0) {
            throw new BadRequestException("Demand must be >= 0");
        }
        
        reading.setZone(zone);
        return readingRepository.save(reading);
    }
    
    @Override
    public DemandReading getLatestReading(Long zoneId) {
        return readingRepository.findFirstByZoneIdOrderByRecordedAtDesc(zoneId)
            .orElseThrow(() -> new ResourceNotFoundException("No readings found for zone: " + zoneId));
    }
    
    @Override
    public List<DemandReading> getReadingsForZone(Long zoneId) {
        // Verify zone exists
        zoneRepository.findById(zoneId)
            .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + zoneId));
        
        return readingRepository.findByZoneIdOrderByRecordedAtDesc(zoneId);
    }
    
    @Override
    public List<DemandReading> getRecentReadings(Long zoneId, int limit) {
        // Verify zone exists
        zoneRepository.findById(zoneId)
            .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + zoneId));
        
        List<DemandReading> allReadings = readingRepository.findByZoneIdOrderByRecordedAtDesc(zoneId);
        
        return allReadings.stream()
            .limit(limit)
            .collect(Collectors.toList());
    }
}