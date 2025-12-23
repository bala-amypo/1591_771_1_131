package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ZoneRestorationRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "zone_id") private Zone zone;
    private Instant restoredAt;
    private Long eventId;
    private String notes;

    public Long getId() { return id; }
    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }
    public Instant getRestoredAt() { return restoredAt; }
    public void setRestoredAt(Instant restoredAt) { this.restoredAt = restoredAt; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}