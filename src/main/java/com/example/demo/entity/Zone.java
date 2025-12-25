package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "zones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String zoneName;
    
    @Column(nullable = false)
    private Integer priorityLevel;
    
    private Integer population;
    
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
    
    @Builder.Default
    private Instant createdAt = Instant.now();
    
    private Instant updatedAt;
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}