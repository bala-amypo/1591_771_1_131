package com.example.demo.repository;

import com.example.demo.entity.LoadSheddingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadSheddingEventRepository extends JpaRepository<LoadSheddingEvent, Long> {
    List<LoadSheddingEvent> findByZoneIdOrderByEventStartDesc(Long zoneId);
}