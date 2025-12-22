package com.example.demo.service.impl;

import com.example.demo.entity.Zone;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.service.ZoneService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    // Constructor injection only
    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    @Override
    public Zone updateZone(Long id, Zone zoneDetails) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        
        zone.setName(zoneDetails.getName());
        zone.setPriorityLevel(zoneDetails.getPriorityLevel());
        zone.setActive(zoneDetails.getActive());
        
        return zoneRepository.save(zone);
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
    public List<Zone> getActiveZones() {
        return zoneRepository.findByActiveTrueOrderByPriorityLevelAsc();
    }
}