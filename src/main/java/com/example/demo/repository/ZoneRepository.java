package com.example.demo.repository;

import com.example.demo.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    
    // Finds active zones ordered by priority level
    // (Used by LoadSheddingService to pick candidates)
    List<Zone> findByActiveTrueOrderByPriorityLevelAsc();
    
    // Some implementations might require Descending order to find 
    // lowest priority (highest number) first
    List<Zone> findByActiveTrueOrderByPriorityLevelDesc();
}