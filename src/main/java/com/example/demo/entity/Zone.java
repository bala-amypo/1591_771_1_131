package com.example.demo.entity;

import java.security.Timestamp;

import org.springframework.stereotype.Indexed;

import jakarta.annotation.Generated;
@Entity

public class Zone {
    @id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,unique=true)
    private String userId;
    
    private String zoneName;
    private int priorityLevel;
    private int poputlation;
    private Boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt; 
    public Zone(){
    }
    public Zone(Long id, String zoneName, int priorityLevel, int poputlation, Boolean active, Timestamp createdAt,
            Timestamp updatedAt) {
        this.id = id;
        this.zoneName = zoneName;
        this.priorityLevel = priorityLevel;
        this.poputlation = poputlation;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public Long getId() {
        return id;
    }
    public String getZoneName() {
        return zoneName;
    }
    public int getPriorityLevel() {
        return priorityLevel;
    }
    public int getPoputlation() {
        return poputlation;
    }
    public Boolean getActive() {
        return active;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
    public void setPoputlation(int poputlation) {
        this.poputlation = poputlation;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
      
}
