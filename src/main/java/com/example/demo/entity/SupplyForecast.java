package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "supply_forecasts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    
    @Builder.Default
    private Instant generatedAt = Instant.now();
}
