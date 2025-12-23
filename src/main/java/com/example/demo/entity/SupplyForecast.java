package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "supply_forecast") 
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "available_supplymw")
    private Double availableSupplyMW;

    private Instant forecastStart;
    private Instant forecastEnd;
    private Instant generatedAt;

    @PrePersist
    protected void onCreate() {
        this.generatedAt = Instant.now(); 
    }
}