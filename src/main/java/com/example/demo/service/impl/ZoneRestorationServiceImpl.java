package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.ZoneRestorationService;
import org.springframework.stereotype.Service;

@Service
public class ZoneRestorationServiceImpl implements ZoneRestorationService {

    private final ZoneRestorationRecordRepository recordRepo;
    private final LoadSheddingEventRepository eventRepo;
    private final ZoneRepository zoneRepo;

    public ZoneRestorationServiceImpl(
            ZoneRestorationRecordRepository recordRepo,
            LoadSheddingEventRepository eventRepo,
            ZoneRepository zoneRepo) {

        this.recordRepo = recordRepo;
        this.eventRepo = eventRepo;
        this.zoneRepo = zoneRepo;
    }

    @Override
    public ZoneRestorationRecord restoreZone(ZoneRestorationRecord record) {

        LoadSheddingEvent event = eventRepo.findById(record.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        zoneRepo.findById(record.getZone().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found"));

        if (record.getRestoredAt().isBefore(event.getEventStart())) {
            throw new BadRequestException("Restored time must be after event start");
        }

        return recordRepo.save(record);
    }

    @Override
    public ZoneRestorationRecord getRecordById(Long id) {
        return recordRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));
    }

    @Override
    public java.util.List<ZoneRestorationRecord> getRecordsForZone(Long zoneId) {
        return recordRepo.findByZoneIdOrderByRestoredAtDesc(zoneId);
    }
}
