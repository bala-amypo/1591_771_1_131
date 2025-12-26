package com.example.demo.service.impl;

import com.example.demo.entity.LoadSheddingEvent;
import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoadSheddingEventRepository;
import com.example.demo.repository.ZoneRepository;
import com.example.demo.repository.ZoneRestorationRecordRepository;
import com.example.demo.service.ZoneRestorationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneRestorationServiceImpl implements ZoneRestorationService {
    
    private final ZoneRestorationRecordRepository zoneRestorationRecordRepository;
    private final LoadSheddingEventRepository loadSheddingEventRepository;
    private final ZoneRepository zoneRepository;
    
    public ZoneRestorationServiceImpl(ZoneRestorationRecordRepository zoneRestorationRecordRepository,
                                      LoadSheddingEventRepository loadSheddingEventRepository,
                                      ZoneRepository zoneRepository) {
        this.zoneRestorationRecordRepository = zoneRestorationRecordRepository;
        this.loadSheddingEventRepository = loadSheddingEventRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public ZoneRestorationRecord restoreZone(ZoneRestorationRecord record) {
        LoadSheddingEvent event = loadSheddingEventRepository.findById(record.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        
        zoneRepository.findById(record.getZone().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));
        
        if (record.getRestoredAt().isBefore(event.getEventStart())) {
            throw new BadRequestException("Restoration time must be after event start");
        }
        
        return zoneRestorationRecordRepository.save(record);
    }

    @Override
    public ZoneRestorationRecord getRecordById(Long id) {
        return zoneRestorationRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
    }

    @Override
    public List<ZoneRestorationRecord> getRecordsForZone(Long zoneId) {
        return zoneRestorationRecordRepository.findByZoneIdOrderByRestoredAtDesc(zoneId);
    }
}