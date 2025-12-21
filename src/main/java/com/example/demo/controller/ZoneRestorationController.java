package com.example.demo.controller;

import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.service.ZoneRestorationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restorations")
public class ZoneRestorationController {

    private final ZoneRestorationService service;

    public ZoneRestorationController(ZoneRestorationService service) {
        this.service = service;
    }

    @PostMapping
    public ZoneRestorationRecord restore(@RequestBody ZoneRestorationRecord r) {
        return service.restoreZone(r);
    }

    @GetMapping("/{id}")
    public ZoneRestorationRecord get(@PathVariable Long id) {
        return service.getRecordById(id);
    }

    @GetMapping("/zone/{zoneId}")
    public List<ZoneRestorationRecord> list(@PathVariable Long zoneId) {
        return service.getRecordsForZone(zoneId);
    }
}
