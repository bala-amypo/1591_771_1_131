package com.example.demo.service.impl;

import com.example.demo.entity.DemandReading;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.DemandReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DemandReadingServiceImpl implements DemandReadingService {

    private final DemandReadingRepository readingRepo;
    private final ZoneRepository zoneRepo;

    @Override
    public DemandReading createReading(DemandReading reading) {
        // Rule: Zone must exist
        if (reading.getZone() == null || reading.getZone().getId() == null || 
            !zoneRepo.existsById(reading.getZone().getId())) {
            throw new ResourceNotFoundException("Zone not found");
        }

        // Rule: demandMW >= 0
        if (reading.getDemandMW() == null || reading.getDemandMW() < 0) {
            throw new BadRequestException("demandMW must be >= 0");
        }

        // Rule: recordedAt not in the future
        if (reading.getRecordedAt() != null && reading.getRecordedAt().isAfter(Instant.now())) {
            throw new BadRequestException("recordedAt must not be in the future");
        }

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
}