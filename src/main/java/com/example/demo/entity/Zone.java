package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "zones")
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String zoneName;

    @Column(nullable = false)
    private Integer priorityLevel;

    private Integer population;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    public Zone() {}

    public Zone(String zoneName, Integer priorityLevel, Integer population, Boolean active) {
        this.zoneName = zoneName;
        this.priorityLevel = priorityLevel;
        this.population = population;
        this.active = active != null ? active : true;
    }

    public static ZoneBuilder builder() {
        return new ZoneBuilder();
    }

    public static class ZoneBuilder {
        private Long id;
        private String zoneName;
        private Integer priorityLevel;
        private Integer population;
        private Boolean active = true;

        public ZoneBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ZoneBuilder zoneName(String zoneName) {
            this.zoneName = zoneName;
            return this;
        }

        public ZoneBuilder priorityLevel(Integer priorityLevel) {
            this.priorityLevel = priorityLevel;
            return this;
        }

        public ZoneBuilder population(Integer population) {
            this.population = population;
            return this;
        }

        public ZoneBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Zone build() {
            Zone zone = new Zone(zoneName, priorityLevel, population, active);
            zone.setId(id);
            return zone;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }

    public Integer getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(Integer priorityLevel) { this.priorityLevel = priorityLevel; }

    public Integer getPopulation() { return population; }
    public void setPopulation(Integer population) { this.population = population; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}