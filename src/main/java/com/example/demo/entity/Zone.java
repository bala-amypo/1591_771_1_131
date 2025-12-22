package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // 1 (Highest) to 10 (Lowest)
    private Integer priorityLevel;

    private Boolean active;

    // Constructors
    public Zone() {}

    public Zone(String name, Integer priorityLevel, Boolean active) {
        this.name = name;
        this.priorityLevel = priorityLevel;
        this.active = active;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(Integer priorityLevel) { this.priorityLevel = priorityLevel; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}