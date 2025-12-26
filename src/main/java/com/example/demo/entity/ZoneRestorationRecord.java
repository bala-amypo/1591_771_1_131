package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "zone_restoration_records")
public class ZoneRestorationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private Instant restoredAt;

    @Column(nullable = false)
    private Long eventId;

    private String notes;

    public ZoneRestorationRecord() {}

    public ZoneRestorationRecord(Zone zone, Instant restoredAt, Long eventId, String notes) {
        this.zone = zone;
        this.restoredAt = restoredAt;
        this.eventId = eventId;
        this.notes = notes;
    }

    public static ZoneRestorationRecordBuilder builder() {
        return new ZoneRestorationRecordBuilder();
    }

    public static class ZoneRestorationRecordBuilder {
        private Zone zone;
        private Instant restoredAt;
        private Long eventId;
        private String notes;

        public ZoneRestorationRecordBuilder zone(Zone zone) {
            this.zone = zone;
            return this;
        }

        public ZoneRestorationRecordBuilder restoredAt(Instant restoredAt) {
            this.restoredAt = restoredAt;
            return this;
        }

        public ZoneRestorationRecordBuilder eventId(Long eventId) {
            this.eventId = eventId;
            return this;
        }

        public ZoneRestorationRecordBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public ZoneRestorationRecord build() {
            return new ZoneRestorationRecord(zone, restoredAt, eventId, notes);
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }

    public Instant getRestoredAt() { return restoredAt; }
    public void setRestoredAt(Instant restoredAt) { this.restoredAt = restoredAt; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}