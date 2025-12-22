package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "supplyForecast")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double availableSupplyMW;
    private Instant forecastStart;
    private Instant forecastEnd;
    private Instant generatedAt;

    @PrePersist
    protected void onPersist() {
        this.generatedAt = Instant.now(); // Auto-set on creation
    }
}