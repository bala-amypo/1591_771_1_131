package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.DemandReadingService;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandReadingServiceImpl implements DemandReadingService {

    private final DemandReadingRepository readingRepo;
    private final ZoneRepository zoneRepo;

    public DemandReadingServiceImpl(DemandReadingRepository readingRepo, ZoneRepository zoneRepo) {
        this.readingRepo = readingRepo;
        this.zoneRepo = zoneRepo;
    }

    @Override
    public DemandReading createReading(DemandReading reading) {
        if (reading.getZone() == null || reading.getZone().getId() == null) {
            throw new ResourceNotFoundException("Zone not found");
        }
        
        Zone zone = zoneRepo.findById(reading.getZone().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        if (reading.getDemandMW() != null && reading.getDemandMW() < 0) {
            throw new BadRequestException("demandMW must be >= 0");
        }

        if (reading.getRecordedAt() != null && reading.getRecordedAt().isAfter(Instant.now())) {
            throw new BadRequestException("recordedAt must not be in the future");
        }

        reading.setZone(zone);
        return readingRepo.save(reading);
    }

    @Override
    public List<DemandReading> getReadingsForZone(Long zoneId) {
        if (!zoneRepo.existsById(zoneId)) {
            throw new ResourceNotFoundException("Zone not found");
        }
        return readingRepo.findByZoneIdOrderByRecordedAtDesc(zoneId);
    }

    @Override
    public DemandReading getLatestReading(Long zoneId) {
        return readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("No readings"));
    }

    @Override
    public List<DemandReading> getRecentReadings(Long zoneId, int limit) {
        List<DemandReading> readings = readingRepo.findByZoneIdOrderByRecordedAtDesc(zoneId);
        return readings.stream().limit(limit).collect(Collectors.toList());
    }
} // <--- ENSURE NO CHARACTERS EXIST AFTER THIS BRACE