package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplyForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double availableSupplyMW;

    @Column(nullable = false)
    private Instant forecastStart;

    @Column(nullable = false)
    private Instant forecastEnd;

    private Instant generatedAt;

    @PrePersist
    public void onCreate() {
        this.generatedAt = Instant.now();
    }
}
