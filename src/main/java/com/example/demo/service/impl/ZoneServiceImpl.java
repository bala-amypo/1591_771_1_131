package com.example.demo.service.impl;

import com.example.demo.entity.Zone;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.ZoneService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepo;

    // Constructor injection in exact order for testing
    public ZoneServiceImpl(ZoneRepository zoneRepo) {
        this.zoneRepo = zoneRepo;
    }

    @Override
    public Zone createZone(Zone zone) {
        if (zone.getPriorityLevel() < 1) {
            throw new RuntimeException("priorityLevel must be >= 1");
        }
        if (zoneRepo.findByZoneName(zone.getZoneName()).isPresent()) {
            throw new RuntimeException("zoneName must be unique");
        }
        zone.setActive(true);
        return zoneRepo.save(zone);
    }

    @Override
    public Zone updateZone(Long id, Zone zoneDetails) {
        Zone zone = zoneRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        zone.setZoneName(zoneDetails.getZoneName());
        zone.setPriorityLevel(zoneDetails.getPriorityLevel());
        zone.setPopulation(zoneDetails.getPopulation());
        // updatedAt is handled by @UpdateTimestamp in entity
        return zoneRepo.save(zone);
    }

    @Override
    public Zone getZoneById(Long id) {
        return zoneRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
    }

    @Override
    public List<Zone> getAllZones() {
        return zoneRepo.findAll();
    }

    @Override
    public void deactivateZone(Long id) {
        Zone zone = zoneRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        zone.setActive(false);
        zoneRepo.save(zone);
    }
}