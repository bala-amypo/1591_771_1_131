package com.example.demo.service.impl;

import com.example.demo.entity.Zone;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.ZoneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    // Strict constructor injection as required
    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone createZone(Zone zone) {
        // Validation: Priority >= 1
        if (zone.getPriorityLevel() < 1) {
            throw new RuntimeException("priorityLevel must be >= 1");
        }
        // Validation: Unique Name
        if (zoneRepository.findByZoneName(zone.getZoneName()).isPresent()) {
            throw new RuntimeException("zoneName must be unique");
        }
        zone.setActive(true); // Default value
        return zoneRepository.save(zone);
    }

    @Override
    public Zone updateZone(Long id, Zone zoneDetails) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        
        zone.setZoneName(zoneDetails.getZoneName());
        zone.setPriorityLevel(zoneDetails.getPriorityLevel());
        zone.setPopulation(zoneDetails.getPopulation());
        return zoneRepository.save(zone);
    }

    @Override
    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
    }

    @Override
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    @Override
    public void deactivateZone(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone not found"));
        zone.setActive(false);
        zoneRepository.save(zone);
    }
}