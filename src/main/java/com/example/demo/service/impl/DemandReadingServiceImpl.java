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

@Service
public class DemandReadingServiceImpl implements DemandReadingService {

    private final DemandReadingRepository readingRepo;
    private final ZoneRepository zoneRepo;

    public DemandReadingServiceImpl(DemandReadingRepository readingRepo,
                                    ZoneRepository zoneRepo) {
        this.readingRepo = readingRepo;
        this.zoneRepo = zoneRepo;
    }

    @Override
    public DemandReading createReading(DemandReading reading) {

        Zone zone = zoneRepo.findById(reading.getZone().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        if (reading.getDemandMW() < 0) {
            throw new BadRequestException("Demand must be >= 0");
        }

        if (reading.getRecordedAt().isAfter(Instant.now())) {
            throw new BadRequestException("Timestamp cannot be in future");
        }

        reading.setZone(zone);
        return readingRepo.save(reading);
    }

    @Override
    public List<DemandReading> getReadingsForZone(Long zoneId) {
        zoneRepo.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        return readingRepo.findByZoneIdOrderByRecordedAtDesc(zoneId);
    }

    @Override
    public DemandReading getLatestReading(Long zoneId) {
        return readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("No readings"));
    }

    @Override
    public List<DemandReading> getRecentReadings(Long zoneId, int limit) {
        List<DemandReading> all = getReadingsForZone(zoneId);
        return all.subList(0, Math.min(limit, all.size()));
    }
}
