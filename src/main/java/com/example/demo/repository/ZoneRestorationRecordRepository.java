package com.example.demo.repository;

import com.example.demo.entity.ZoneRestorationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ZoneRestorationRecordRepository extends JpaRepository<ZoneRestorationRecord, Long> {
    List<ZoneRestorationRecord> findByZoneIdOrderByRestoredAtDesc(Long zoneId);
}