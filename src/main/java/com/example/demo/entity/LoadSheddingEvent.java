package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "load_shedding_events")
public class LoadSheddingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private Instant eventStart;

    private Instant eventEnd;

    private String reason;

    private Long triggeredByForecastId;

    @Column(nullable = false)
    private Double expectedDemandReductionMW;

    public LoadSheddingEvent() {}

    public LoadSheddingEvent(Zone zone, Instant eventStart, String reason, Long triggeredByForecastId, Double expectedDemandReductionMW) {
        this.zone = zone;
        this.eventStart = eventStart;
        this.reason = reason;
        this.triggeredByForecastId = triggeredByForecastId;
        this.expectedDemandReductionMW = expectedDemandReductionMW;
    }

    public static LoadSheddingEventBuilder builder() {
        return new LoadSheddingEventBuilder();
    }

    public static class LoadSheddingEventBuilder {
        private Long id;
        private Zone zone;
        private Instant eventStart;
        private Instant eventEnd;
        private String reason;
        private Long triggeredByForecastId;
        private Double expectedDemandReductionMW;

        public LoadSheddingEventBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public LoadSheddingEventBuilder zone(Zone zone) {
            this.zone = zone;
            return this;
        }

        public LoadSheddingEventBuilder eventStart(Instant eventStart) {
            this.eventStart = eventStart;
            return this;
        }

        public LoadSheddingEventBuilder eventEnd(Instant eventEnd) {
            this.eventEnd = eventEnd;
            return this;
        }

        public LoadSheddingEventBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public LoadSheddingEventBuilder triggeredByForecastId(Long triggeredByForecastId) {
            this.triggeredByForecastId = triggeredByForecastId;
            return this;
        }

        public LoadSheddingEventBuilder expectedDemandReductionMW(Double expectedDemandReductionMW) {
            this.expectedDemandReductionMW = expectedDemandReductionMW;
            return this;
        }

        public LoadSheddingEvent build() {
            LoadSheddingEvent event = new LoadSheddingEvent(zone, eventStart, reason, triggeredByForecastId, expectedDemandReductionMW);
            event.setId(id);
            event.setEventEnd(eventEnd);
            return event;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }

    public Instant getEventStart() { return eventStart; }
    public void setEventStart(Instant eventStart) { this.eventStart = eventStart; }

    public Instant getEventEnd() { return eventEnd; }
    public void setEventEnd(Instant eventEnd) { this.eventEnd = eventEnd; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Long getTriggeredByForecastId() { return triggeredByForecastId; }
    public void setTriggeredByForecastId(Long triggeredByForecastId) { this.triggeredByForecastId = triggeredByForecastId; }

    public Double getExpectedDemandReductionMW() { return expectedDemandReductionMW; }
    public void setExpectedDemandReductionMW(Double expectedDemandReductionMW) { this.expectedDemandReductionMW = expectedDemandReductionMW; }
}