package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.DemandReadingService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DemandReadingServiceImpl implements DemandReadingService {
    
    private final DemandReadingRepository demandReadingRepository;
    private final ZoneRepository zoneRepository;
    
    public DemandReadingServiceImpl(DemandReadingRepository demandReadingRepository, ZoneRepository zoneRepository) {
        this.demandReadingRepository = demandReadingRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public DemandReading createReading(DemandReading reading) {
        if (reading.getDemandMW() < 0) {
            throw new BadRequestException("Demand must be >= 0");
        }
        
        if (reading.getRecordedAt().isAfter(Instant.now())) {
            throw new BadRequestException("Recorded time cannot be in the future");
        }
        
        if (reading.getZone() != null && reading.getZone().getId() != null) {
            zoneRepository.findById(reading.getZone().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        }
        
        return demandReadingRepository.save(reading);
    }

    @Override
    public List<DemandReading> getReadingsForZone(Long zoneId) {
        zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        
        return demandReadingRepository.findByZoneIdOrderByRecordedAtDesc(zoneId);
    }

    @Override
    public DemandReading getLatestReading(Long zoneId) {
        return demandReadingRepository.findFirstByZoneIdOrderByRecordedAtDesc(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("No readings"));
    }

    @Override
    public List<DemandReading> getRecentReadings(Long zoneId, int limit) {
        List<DemandReading> readings = demandReadingRepository.findByZoneIdOrderByRecordedAtDesc(zoneId);
        return readings.size() <= limit ? readings : readings.subList(0, limit);
    }
}