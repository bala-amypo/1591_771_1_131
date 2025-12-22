package com.example.demo.repository;

import com.example.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Optional<Zone> findByZoneName(String zoneName);
    List<Zone> findByActiveTrueOrderByPriorityLevelAsc();
}

public interface DemandReadingRepository extends JpaRepository<DemandReading, Long> {
    Optional<DemandReading> findFirstByZoneIdOrderByRecordedAtDesc(Long zoneId);
    List<DemandReading> findByZoneIdOrderByRecordedAtDesc(Long zoneId);
}

public interface SupplyForecastRepository extends JpaRepository<SupplyForecast, Long> {
    Optional<SupplyForecast> findFirstByOrderByGeneratedAtDesc();
}

public interface LoadSheddingEventRepository extends JpaRepository<LoadSheddingEvent, Long> {
    List<LoadSheddingEvent> findByZoneIdOrderByEventStartDesc(Long zoneId);
}

public interface ZoneRestorationRecordRepository extends JpaRepository<ZoneRestorationRecord, Long> {
    List<ZoneRestorationRecord> findByZoneIdOrderByRestoredAtDesc(Long zoneId);
}