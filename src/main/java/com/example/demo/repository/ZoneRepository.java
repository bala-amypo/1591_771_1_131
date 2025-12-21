package com.example.demo.repository;

import com.example.demo.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Optional<Zone> findByZoneName(String zoneName);
    
    // Used for selecting zones to shed (lowest priority first)
    List<Zone> findByActiveTrueOrderByPriorityLevelAsc();
}