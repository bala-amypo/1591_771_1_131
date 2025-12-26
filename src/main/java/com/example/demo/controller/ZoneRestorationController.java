package com.example.demo.controller;

import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.service.ZoneRestorationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restorations")
public class ZoneRestorationController {
    
    private final ZoneRestorationService zoneRestorationService;
    
    public ZoneRestorationController(ZoneRestorationService zoneRestorationService) {
        this.zoneRestorationService = zoneRestorationService;
    }

    @PostMapping
    public ResponseEntity<ZoneRestorationRecord> restoreZone(@RequestBody ZoneRestorationRecord record) {
        ZoneRestorationRecord restorationRecord = zoneRestorationService.restoreZone(record);
        return ResponseEntity.ok(restorationRecord);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneRestorationRecord> getRecord(@PathVariable Long id) {
        ZoneRestorationRecord record = zoneRestorationService.getRecordById(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<ZoneRestorationRecord>> getRecordsForZone(@PathVariable Long zoneId) {
        List<ZoneRestorationRecord> records = zoneRestorationService.getRecordsForZone(zoneId);
        return ResponseEntity.ok(records);
    }
}