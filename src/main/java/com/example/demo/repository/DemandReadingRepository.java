package com.example.demo.repository;

import com.example.demo.entity.DemandReading;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DemandReadingRepository extends JpaRepository<DemandReading, Long> {
    // Find the single most recent reading for a zone
    Optional<DemandReading> findFirstByZoneIdOrderByRecordedAtDesc(Long zoneId);
    
    // Find all readings for a zone, sorted by time
    List<DemandReading> findByZoneIdOrderByRecordedAtDesc(Long zoneId);
}