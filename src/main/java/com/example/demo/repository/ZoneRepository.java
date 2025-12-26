package com.example.demo.repository;

import com.example.demo.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Optional<Zone> findByZoneName(String zoneName);
    List<Zone> findByActiveTrueOrderByPriorityLevelAsc();
}