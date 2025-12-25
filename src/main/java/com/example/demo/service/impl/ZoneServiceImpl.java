// ZoneServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.Zone;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {
    
    private final ZoneRepository zoneRepository;
    
    @Override
    @Transactional
    public Zone createZone(Zone zone) {
        // Validate zone name uniqueness
        if (zoneRepository.findByZoneName(zone.getZoneName()).isPresent()) {
            throw new BadRequestException("Zone name must be unique");
        }
        
        // Validate priority level
        if (zone.getPriorityLevel() == null || zone.getPriorityLevel() < 1) {
            throw new BadRequestException("Priority level must be >= 1");
        }
        
        // Set defaults
        if (zone.getActive() == null) {
            zone.setActive(true);
        }
        
        zone.setCreatedAt(Instant.now());
        
        return zoneRepository.save(zone);
    }
    
    @Override
    @Transactional
    public Zone updateZone(Long id, Zone zone) {
        Zone existing = zoneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));
        
        if (zone.getZoneName() != null) {
            existing.setZoneName(zone.getZoneName());
        }
        if (zone.getPriorityLevel() != null) {
            existing.setPriorityLevel(zone.getPriorityLevel());
        }
        if (zone.getPopulation() != null) {
            existing.setPopulation(zone.getPopulation());
        }
        if (zone.getActive() != null) {
            existing.setActive(zone.getActive());
        }
        
        existing.setUpdatedAt(Instant.now());
        
        return zoneRepository.save(existing);
    }
    
    @Override
    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));
    }
    
    @Override
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }
    
    @Override
    @Transactional
    public void deactivateZone(Long id) {
        Zone zone = zoneRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));
        
        zone.setActive(false);
        zoneRepository.save(zone);
    }
}