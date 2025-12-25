package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "demand_readings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;
    
    @Column(nullable = false)
    private Double demandMW;
    
    @Column(nullable = false)
    private Instant recordedAt;
}