package com.example.demo.repository;

import com.example.demo.entity.LoadSheddingEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoadSheddingEventRepository
        extends JpaRepository<LoadSheddingEvent, Long> {

    List<LoadSheddingEvent> findByZoneIdOrderByEventStartDesc(Long zoneId);
}
