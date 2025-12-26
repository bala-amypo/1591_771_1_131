package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "demand_readings")
public class DemandReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private Double demandMW;

    @Column(nullable = false)
    private Instant recordedAt;

    public DemandReading() {}

    public DemandReading(Zone zone, Double demandMW, Instant recordedAt) {
        this.zone = zone;
        this.demandMW = demandMW;
        this.recordedAt = recordedAt;
    }

    public static DemandReadingBuilder builder() {
        return new DemandReadingBuilder();
    }

    public static class DemandReadingBuilder {
        private Long id;
        private Zone zone;
        private Double demandMW;
        private Instant recordedAt;

        public DemandReadingBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DemandReadingBuilder zone(Zone zone) {
            this.zone = zone;
            return this;
        }

        public DemandReadingBuilder demandMW(Double demandMW) {
            this.demandMW = demandMW;
            return this;
        }

        public DemandReadingBuilder recordedAt(Instant recordedAt) {
            this.recordedAt = recordedAt;
            return this;
        }

        public DemandReading build() {
            DemandReading reading = new DemandReading(zone, demandMW, recordedAt);
            reading.setId(id);
            return reading;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }

    public Double getDemandMW() { return demandMW; }
    public void setDemandMW(Double demandMW) { this.demandMW = demandMW; }

    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }
}