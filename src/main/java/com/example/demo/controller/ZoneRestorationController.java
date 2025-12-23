package com.example.demo.controller;
import com.example.demo.entity.ZoneRestorationRecord;
import com.example.demo.service.ZoneRestorationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/restorations") @CrossOrigin("*")
public class ZoneRestorationController {
    private final ZoneRestorationService service;
    public ZoneRestorationController(ZoneRestorationService service) { this.service = service; }

    @PostMapping("/")
    public ZoneRestorationRecord create(@RequestBody ZoneRestorationRecord record) {
        return service.restoreZone(record);
    }

    @GetMapping("/zone/{zoneId}")
    public List<ZoneRestorationRecord> getByZone(@PathVariable Long zoneId) {
        return service.getRecordsForZone(zoneId);
    }
}