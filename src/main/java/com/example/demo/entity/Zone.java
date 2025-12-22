package com.example.demo.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.Instant;

@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Zone {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String zoneName;
    private Integer priorityLevel;
    private Integer population;
    private Boolean active = true;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = Instant.now(); updatedAt = Instant.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = Instant.now(); }
}

@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DemandReading {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "zone_id")
    private Zone zone;
    private Double demandMW;
    private Instant recordedAt;
}

@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SupplyForecast {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double availableSupplyMW;
    private Instant forecastStart;
    private Instant forecastEnd;
    private Instant generatedAt;

    @PrePersist
    protected void onGenerate() { generatedAt = Instant.now(); }
}

@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LoadSheddingEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "zone_id")
    private Zone zone;
    private Instant eventStart;
    private Instant eventEnd;
    private String reason;
    private Long triggeredByForecastId;
    private Double expectedDemandReductionMW;
}

@Entity @Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ZoneRestorationRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "zone_id")
    private Zone zone;
    private Instant restoredAt;
    private Long eventId;
    private String notes;
}