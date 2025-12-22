package com.example.demo.service.impl;

import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
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
        if (zone.getPriorityLevel() != null && zone.getPriorityLevel() < 1) {
            throw new BadRequestException("priorityLevel must be >= 1");
        }
        if (zoneRepository.findByZoneName(zone.getZoneName()).isPresent()) {
            throw new BadRequestException("zoneName must be unique");
        }
        // active = true is handled by Lombok @Builder.Default or Entity default
        return zoneRepository.save(zone);
    }

    @Override
    public Zone updateZone(Long id, Zone zoneDetails) {
        Zone existingZone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        existingZone.setZoneName(zoneDetails.getZoneName());
        existingZone.setPriorityLevel(zoneDetails.getPriorityLevel());
        existingZone.setPopulation(zoneDetails.getPopulation());
        
        return zoneRepository.save(existingZone);
    }

    @Override
    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
    }

    @Override
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    @Override
    public void deactivateZone(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        zone.setActive(false);
        zoneRepository.save(zone);
    }
}