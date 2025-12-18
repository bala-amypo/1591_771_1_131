package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "zone")
public class Zone {

    @Id
    private int id;
    private String zoneName;
    private int priorityLevel;
    private int population;
    private boolean active;

    public Zone() {
    }

    public Zone(int id, String zoneName, int priorityLevel, int population, boolean active) {
        this.id = id;
        this.zoneName = zoneName;
        this.priorityLevel = priorityLevel;
        this.population = population;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
