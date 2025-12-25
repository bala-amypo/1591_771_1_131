// ZoneRestorationServiceImpl.java
package com.example.demo.service.impl;

import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.entity.Zone;
import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoadSheddingEventRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.repository.ZoneRestorationRecordRepository;
import com.example.demo.service.ZoneRestorationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneRestorationServiceImpl implements ZoneRestorationService {
    
    private final ZoneRestorationRecordRepository restorationRepository;
    private final LoadSheddingEventRepository eventRepository;
    private final ZoneRepository zoneRepository;
    
    @Override
    @Transactional
    public ZoneRestorationRecord restoreZone(ZoneRestorationRecord record) {
        // Validate event exists
        LoadSheddingEvent event = eventRepository.findById(record.getEventId())
            .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + record.getEventId()));
        
        // Validate zone exists
        Long zoneId = record.getZone().getId();
        Zone zone = zoneRepository.findById(zoneId)
            .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + zoneId));
        
        // Validate restoration time is after event start
        if (record.getRestoredAt().isBefore(event.getEventStart())) {
            throw new BadRequestException("Restoration time must be after event start");
        }
        
        record.setZone(zone);
        return restorationRepository.save(record);
    }
    
    @Override
    public ZoneRestorationRecord getRecordById(Long id) {
        return restorationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
    }
    
    @Override
    public List<ZoneRestorationRecord> getRecordsForZone(Long zoneId) {
        return restorationRepository.findByZoneIdOrderByRestoredAtDesc(zoneId);
    }
}