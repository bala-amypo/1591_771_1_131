package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "load_shedding_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadSheddingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone; // The zone affected by shedding

    private Instant eventStart;
    private Instant eventEnd; // Optional initially
    private String reason;
    private Long triggeredByForecastId; // Reference to the forecast that caused the trigger
    private Double expectedDemandReductionMW; // Rule: Must be >= 0
}