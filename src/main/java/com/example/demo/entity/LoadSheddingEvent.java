package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "load_shedding_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadSheddingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;
    
    @Column(nullable = false)
    private Instant eventStart;
    
    private Instant eventEnd;
    
    private String reason;
    
    private Double expectedDemandReductionMW;
    
    @Builder.Default
    private Instant createdAt = Instant.now();
}