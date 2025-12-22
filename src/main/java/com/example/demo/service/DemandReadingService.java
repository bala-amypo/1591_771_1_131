package com.example.demo.service;

import com.example.demo.entity.DemandReading;
import com.example.demo.repository.DemandReadingRepository;
import com.example.demo.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandReadingServiceImpl {

    private final DemandReadingRepository readingRepo;
    private final ZoneRepository zoneRepo;

    public DemandReadingServiceImpl(DemandReadingRepository readingRepo, ZoneRepository zoneRepo) {
        this.readingRepo = readingRepo;
        this.zoneRepo = zoneRepo;
    }

    public DemandReading createReading(DemandReading reading) {
        // Rule: Zone must exist
        if (reading.getZone() == null || !zoneRepo.existsById(reading.getZone().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zone not found");
        }

        // Rule: Demand must be >= 0
        if (reading.getDemandMW() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "demandMW must be >= 0");
        }

        // Rule: Timestamp must not be in the future
        if (reading.getRecordedAt().isAfter(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "recordedAt must not be in the future");
        }

        return readingRepo.save(reading);
    }

    public List<DemandReading> getReadingsForZone(Long zoneId) {
        if (!zoneRepo.existsById(zoneId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zone not found");
        }
        return readingRepo.findByZoneIdOrderByRecordedAtDesc(zoneId);
    }

    public DemandReading getLatestReading(Long zoneId) {
        return readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(zoneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No readings"));
    }

    public List<DemandReading> getRecentReadings(Long zoneId, int limit) {
        List<DemandReading> all = readingRepo.findByZoneIdOrderByRecordedAtDesc(zoneId);
        return all.stream().limit(limit).collect(Collectors.toList());
    }
}