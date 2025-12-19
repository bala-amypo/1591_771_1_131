package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.LoadSheddingService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class LoadSheddingServiceImpl implements LoadSheddingService {

    private final SupplyForecastRepository supplyForecastRepository;
    private final ZoneRepository zoneRepository;
    private final DemandReadingRepository demandReadingRepository;
    private final LoadSheddingEventRepository loadSheddingEventRepository;

    // ORDER IS IMPORTANT
    public LoadSheddingServiceImpl(
            SupplyForecastRepository supplyForecastRepository,
            ZoneRepository zoneRepository,
            DemandReadingRepository demandReadingRepository,
            LoadSheddingEventRepository loadSheddingEventRepository) {

        this.supplyForecastRepository = supplyForecastRepository;
        this.zoneRepository = zoneRepository;
        this.demandReadingRepository = demandReadingRepository;
        this.loadSheddingEventRepository = loadSheddingEventRepository;
    }

    @Override
    public LoadSheddingEvent triggerLoadShedding(Long forecastId) {

        SupplyForecast forecast = supplyForecastRepository.findById(forecastId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Forecast not found"));

        List<Zone> activeZones =
                zoneRepository.findByActiveTrueOrderByPriorityLevelAsc();

        if (activeZones.isEmpty()) {
            throw new BadRequestException("No suitable zones");
        }

        for (Zone zone : activeZones) {

            DemandReading latestReading =
                    demandReadingRepository
                            .findFirstByZoneIdOrderByRecordedAtDesc(zone.getId())
                            .orElse(null);

            if (latestReading == null) continue;

            if (latestReading.getDemandMW()
                    > forecast.getAvailableSupplyMW()) {

                LoadSheddingEvent event = LoadSheddingEvent.builder()
                        .zone(zone)
                        .eventStart(Instant.now())
                        .reason("Demand exceeds supply")
                        .triggeredByForecastId(forecastId)
                        .expectedDemandReductionMW(
                                latestReading.getDemandMW()
                                        - forecast.getAvailableSupplyMW())
                        .build();

                return loadSheddingEventRepository.save(event);
            }
        }

        throw new BadRequestException("No overload");
    }

    @Override
    public LoadSheddingEvent getEventById(Long id) {
        return loadSheddingEventRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Event not found"));
    }

    @Override
    public List<LoadSheddingEvent> getEventsForZone(Long zoneId) {
        return loadSheddingEventRepository
                .findByZoneIdOrderByEventStartDesc(zoneId);
    }

    @Override
    public List<LoadSheddingEvent> getAllEvents() {
        return loadSheddingEventRepository.findAll();
    }
}
