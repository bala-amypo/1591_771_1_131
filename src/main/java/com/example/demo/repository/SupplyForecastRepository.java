package com.example.demo.repository;

import com.example.demo.entity.SupplyForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplyForecastRepository extends JpaRepository<SupplyForecast, Long> {

    Optional<SupplyForecast> findFirstByOrderByGeneratedAtDesc();
}
