package com.example.demo.entity;

import lombok.*;
import jakarta.persistence.*;
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

   
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    
    private Double demandMW;

   
    private Instant recordedAt;

    
}