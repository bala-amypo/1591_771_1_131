package com.example.demo.dto;

import java.time.Instant;

public class ZoneDTO {
    private Long id;
    private String zoneName;
    private Integer priorityLevel;
    private Integer population;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    public ZoneDTO() {}

    public ZoneDTO(Long id, String zoneName, Integer priorityLevel, Integer population, Boolean active, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.zoneName = zoneName;
        this.priorityLevel = priorityLevel;
        this.population = population;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ZoneDTOBuilder builder() {
        return new ZoneDTOBuilder();
    }

    public static class ZoneDTOBuilder {
        private Long id;
        private String zoneName;
        private Integer priorityLevel;
        private Integer population;
        private Boolean active;
        private Instant createdAt;
        private Instant updatedAt;

        public ZoneDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ZoneDTOBuilder zoneName(String zoneName) {
            this.zoneName = zoneName;
            return this;
        }

        public ZoneDTOBuilder priorityLevel(Integer priorityLevel) {
            this.priorityLevel = priorityLevel;
            return this;
        }

        public ZoneDTOBuilder population(Integer population) {
            this.population = population;
            return this;
        }

        public ZoneDTOBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public ZoneDTOBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ZoneDTOBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ZoneDTO build() {
            return new ZoneDTO(id, zoneName, priorityLevel, population, active, createdAt, updatedAt);
        }
    }

    // Getters and Setters
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