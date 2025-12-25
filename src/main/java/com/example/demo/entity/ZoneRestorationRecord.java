package com.example.demo.entity;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "zone_restoration_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneRestorationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;
    
    @Column(nullable = false)
    private Long eventId;
    
    @Column(nullable = false)
    private Instant restoredAt;
    
    private String notes;
}
