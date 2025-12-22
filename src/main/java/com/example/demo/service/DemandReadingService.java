package com.example.demo.service;

import com.example.demo.entity.DemandReading;
import java.util.List;

public interface DemandReadingService {
    /**
     * Records a new demand measurement.
     * Validates zone existence, non-negative demand, and non-future timestamp.
     */
    DemandReading createReading(DemandReading reading);

    /**
     * Returns all history for a specific zone.
     */
    List<DemandReading> getReadingsForZone(Long zoneId);

    /**
     * Gets the most recent reading recorded for a zone.
     */
    DemandReading getLatestReading(Long zoneId);

    /**
     * Gets a specific number of recent readings for a zone.
     */
    List<DemandReading> getRecentReadings(Long zoneId, int limit);
}