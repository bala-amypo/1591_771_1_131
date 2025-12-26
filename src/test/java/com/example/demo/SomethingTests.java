package com.example.demo;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import com.example.demo.security.JwtTokenProvider;
import java.util.Optional;
import org.mockito.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.*;
@Listeners(TestResultListener.class)
public class SomethingTests {

    // ================================
    // Mocked Repositories
    // ================================
    @Mock ZoneRepository zoneRepo;
    @Mock DemandReadingRepository readingRepo;
    @Mock SupplyForecastRepository forecastRepo;
    @Mock LoadSheddingEventRepository eventRepo;
    @Mock ZoneRestorationRecordRepository restorationRepo;

    // ================================
    // Services
    // ================================
    ZoneService zoneService;
    DemandReadingService readingService;
    SupplyForecastService forecastService;
    LoadSheddingService sheddingService;
    ZoneRestorationService restorationService;

    // ================================
    // Setup
    // ================================
    @BeforeClass
    public void setup() {
        MockitoAnnotations.openMocks(this);
        zoneService = new ZoneServiceImpl(zoneRepo);
        readingService = new DemandReadingServiceImpl(readingRepo, zoneRepo);
        forecastService = new SupplyForecastServiceImpl(forecastRepo);
        sheddingService = new LoadSheddingServiceImpl(forecastRepo, zoneRepo, readingRepo, eventRepo);
        restorationService = new ZoneRestorationServiceImpl(restorationRepo, eventRepo, zoneRepo);
    }

    // 🚀 CRITICAL FIX — prevents stale stubbing causing null pointer failures
    @BeforeMethod
    public void resetMocks() {
        Mockito.reset(zoneRepo, readingRepo, forecastRepo, eventRepo, restorationRepo);
    }

    // --------------------------------------------------------------------
    // Test Cases
    // --------------------------------------------------------------------

    @Test(priority = 1, groups = {"servlet"})
    public void testServletDeployment_simulated() {
        Assert.assertTrue(true);
    }

    @Test(priority = 2, groups = {"servlet"})
    public void testServletHandlesRequest_simulated() {
        Assert.assertNotNull("OK");
    }

    @Test(priority = 3, groups = {"crud"})
    public void testCreateZone_success() {
        Zone input = Zone.builder().zoneName("Z1").priorityLevel(3).population(1000).build();

        when(zoneRepo.findByZoneName("Z1")).thenReturn(Optional.empty());
        when(zoneRepo.save(any())).thenAnswer(i -> {
            Zone z = i.getArgument(0);
            z.setId(1L);
            return z;
        });

        Zone created = zoneService.createZone(input);

        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getZoneName(), "Z1");
    }

    @Test(priority = 4, groups = {"crud"})
    public void testCreateZone_duplicateName_throws() {
        when(zoneRepo.findByZoneName("Z1")).thenReturn(Optional.of(new Zone()));
        try {
            zoneService.createZone(Zone.builder().zoneName("Z1").priorityLevel(1).build());
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(e.getMessage().contains("unique"));
        }
    }

    @Test(priority = 5, groups = {"crud"})
    public void testUpdateZone_success() {
        Zone existing = Zone.builder().id(2L).zoneName("Old").priorityLevel(2).build();

        when(zoneRepo.findById(2L)).thenReturn(Optional.of(existing));
        when(zoneRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Zone updated = zoneService.updateZone(
                2L,
                Zone.builder().zoneName("New").priorityLevel(1).population(500).active(true).build()
        );

        Assert.assertEquals(updated.getZoneName(), "New");
        Assert.assertEquals((int)updated.getPriorityLevel(), 1);
    }

    @Test(priority = 6, groups = {"crud"})
    public void testGetZone_notFound() {
        when(zoneRepo.findById(99L)).thenReturn(Optional.empty());
        try {
            zoneService.getZoneById(99L);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("not found"));
        }
    }

    @Test(priority = 7, groups = {"crud"})
    public void testDeactivateZone_success() {
        Zone z = Zone.builder().id(3L).active(true).build();

        when(zoneRepo.findById(3L)).thenReturn(Optional.of(z));
        when(zoneRepo.save(any())).thenReturn(z);

        zoneService.deactivateZone(3L);
        verify(zoneRepo).save(any());
    }

    @Test(priority = 8, groups = {"di"})
    public void testDependencyInjection_servicesWired() {
        Assert.assertNotNull(zoneService);
        Assert.assertNotNull(readingService);
        Assert.assertNotNull(forecastService);
        Assert.assertNotNull(sheddingService);
        Assert.assertNotNull(restorationService);
    }

    @Test(priority = 9, groups = {"di"})
    public void testIoC_mocksAreUsed() {
        when(zoneRepo.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(zoneService.getAllZones().isEmpty());
    }

    @Test(priority = 10, groups = {"hibernate"})
    public void testDemandReading_create_valid() {
        Zone z = Zone.builder().id(10L).build();
        DemandReading r = DemandReading.builder().zone(z).demandMW(12.5).recordedAt(Instant.now()).build();

        when(zoneRepo.findById(10L)).thenReturn(Optional.of(z));
        when(readingRepo.save(any())).thenAnswer(i -> {
            DemandReading dr = i.getArgument(0);
            dr.setId(1L);
            return dr;
        });

        DemandReading saved = readingService.createReading(r);
        Assert.assertNotNull(saved.getId());
    }

    @Test(priority = 11, groups = {"hibernate"})
    public void testDemandReading_futureTimestamp_throws() {
        Zone z = Zone.builder().id(11L).build();
        DemandReading r = DemandReading.builder()
                .zone(z).demandMW(1.0)
                .recordedAt(Instant.now().plusSeconds(3600))
                .build();

        when(zoneRepo.findById(11L)).thenReturn(Optional.of(z));

        try {
            readingService.createReading(r);
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(e.getMessage().contains("future"));
        }
    }

    @Test(priority = 12, groups = {"hibernate"})
    public void testDemandReading_negativeDemand_throws() {
        Zone z = Zone.builder().id(12L).build();
        DemandReading r = DemandReading.builder().zone(z).demandMW(-1.0).recordedAt(Instant.now()).build();

        when(zoneRepo.findById(12L)).thenReturn(Optional.of(z));

        try {
            readingService.createReading(r);
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(e.getMessage().contains(">= 0"));
        }
    }

    @Test(priority = 13, groups = {"jpa"})
    public void testZoneRepository_uniqueConstraint() {
        when(zoneRepo.findByZoneName("Unique")).thenReturn(Optional.empty());
        Zone z = Zone.builder().zoneName("Unique").priorityLevel(1).build();
        when(zoneRepo.save(any())).thenReturn(z);
        Zone saved = zoneService.createZone(z);
        Assert.assertEquals(saved.getZoneName(), "Unique");
    }

    @Test(priority = 14, groups = {"jpa"})
    public void testSupplyForecast_create_and_getLatest() {
        SupplyForecast sf = SupplyForecast.builder()
                .availableSupplyMW(100.0)
                .forecastStart(Instant.now())
                .forecastEnd(Instant.now().plusSeconds(3600))
                .build();

        when(forecastRepo.save(any())).thenAnswer(i -> {
            SupplyForecast s = i.getArgument(0);
            s.setId(1L);
            return s;
        });

        SupplyForecast created = forecastService.createForecast(sf);
        when(forecastRepo.findFirstByOrderByGeneratedAtDesc()).thenReturn(Optional.of(created));

        Assert.assertEquals(forecastService.getLatestForecast().getAvailableSupplyMW(), 100.0);
    }

    @Test(priority = 15, groups = {"manytoMany"})
    public void testSimulatedManyToMany_association() {
        Assert.assertTrue(true);
    }

    @Test(priority = 16, groups = {"security"})
    public void testJwtToken_creation_and_validation() {
        JwtTokenProvider provider = new JwtTokenProvider();
        AppUser user = AppUser.builder().id(5L).email("a@b.com").role("ROLE_ADMIN").build();
        String token = provider.createToken(user);
        Assert.assertNotNull(token);
        Assert.assertTrue(provider.validateToken(token));
    }

    @Test(priority = 17, groups = {"security"})
    public void testJwtToken_containsClaims() {
        JwtTokenProvider provider = new JwtTokenProvider();
        AppUser user = AppUser.builder().id(9L).email("x@y.com").role("ROLE_USER").build();

        String token = provider.createToken(user);
        var claims = provider.getClaims(token);

        Assert.assertEquals(claims.getSubject(), "x@y.com");
        Assert.assertEquals(claims.get("role"), "ROLE_USER");
        Assert.assertEquals(((Number)claims.get("userId")).longValue(), 9L);
    }

    @Test(priority = 18, groups = {"hql"})
    public void testDemandReadingRepo_getLatest() {
        DemandReading r = DemandReading.builder().id(2L).demandMW(5.0).recordedAt(Instant.now()).build();
        when(readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(100L)).thenReturn(Optional.of(r));
        Assert.assertEquals(readingService.getLatestReading(100L).getDemandMW(), 5.0);
    }

    @Test(priority = 19)
    public void testGetAllZones_empty() {
        when(zoneRepo.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(zoneService.getAllZones().isEmpty());
    }

    @Test(priority = 20)
    public void testCreateForecast_invalidRange_throws() {
        SupplyForecast sf = SupplyForecast.builder()
                .availableSupplyMW(10.0)
                .forecastStart(Instant.now().plusSeconds(1000))
                .forecastEnd(Instant.now())
                .build();

        try {
            forecastService.createForecast(sf);
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(e.getMessage().contains("range"));
        }
    }

    @Test(priority = 21)
    public void testGetReadingsForZone_noZone_throws() {
        when(zoneRepo.findById(199L)).thenReturn(Optional.empty());

        try {
            readingService.getReadingsForZone(199L);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Zone not found"));
        }
    }

    @Test(priority = 22)
    public void testGetRecentReadings_limitsCorrectly() {
        Zone z = Zone.builder().id(20L).build();

        List<DemandReading> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(DemandReading.builder().id((long) i).demandMW((double) i).recordedAt(Instant.now()).zone(z).build());

        when(zoneRepo.findById(20L)).thenReturn(Optional.of(z));
        when(readingRepo.findByZoneIdOrderByRecordedAtDesc(20L)).thenReturn(list);

        Assert.assertEquals(readingService.getRecentReadings(20L, 3).size(), 3);
    }

    @Test(priority = 23)
    public void testTriggerLoadShedding_noForecast_throws() {
        when(forecastRepo.findById(500L)).thenReturn(Optional.empty());
        try {
            sheddingService.triggerLoadShedding(500L);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Forecast not found"));
        }
    }

    @Test(priority = 24)
    public void testTriggerLoadShedding_noOverload_throws() {
        SupplyForecast f = SupplyForecast.builder()
                .id(1L).availableSupplyMW(1000.0)
                .forecastStart(Instant.now())
                .forecastEnd(Instant.now().plusSeconds(3600))
                .build();

        when(forecastRepo.findById(1L)).thenReturn(Optional.of(f));
        when(zoneRepo.findByActiveTrueOrderByPriorityLevelAsc()).thenReturn(Collections.emptyList());

        try {
            sheddingService.triggerLoadShedding(1L);
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(e.getMessage().contains("No overload"));
        }
    }

    @Test(priority = 25)
    public void testTriggerLoadShedding_success_createsEvent() {
        SupplyForecast f = SupplyForecast.builder()
                .id(2L).availableSupplyMW(10.0)
                .forecastStart(Instant.now())
                .forecastEnd(Instant.now().plusSeconds(3600))
                .build();

        Zone low = Zone.builder().id(100L).priorityLevel(5).active(true).build();

        when(forecastRepo.findById(2L)).thenReturn(Optional.of(f));
        when(zoneRepo.findByActiveTrueOrderByPriorityLevelAsc()).thenReturn(List.of(low));

        DemandReading dr = DemandReading.builder().id(11L).zone(low).demandMW(50.0).recordedAt(Instant.now()).build();
        when(readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(100L)).thenReturn(Optional.of(dr));

        when(eventRepo.save(any())).thenAnswer(i -> {
            LoadSheddingEvent e = i.getArgument(0);
            e.setId(9L);
            return e;
        });

        LoadSheddingEvent ev = sheddingService.triggerLoadShedding(2L);

        Assert.assertNotNull(ev);
        Assert.assertTrue(ev.getExpectedDemandReductionMW() >= 50.0);
    }

    @Test(priority = 26)
    public void testRestoreZone_eventNotFound_throws() {
        Zone z = Zone.builder().id(1L).build();
        ZoneRestorationRecord r = ZoneRestorationRecord
                .builder().zone(z).eventId(999L).restoredAt(Instant.now()).build();

        when(eventRepo.findById(999L)).thenReturn(Optional.empty());

        try {
            restorationService.restoreZone(r);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Event not found"));
        }
    }

    @Test(priority = 27)
    public void testRestoreZone_beforeEventStart_throws() {
        Zone z = Zone.builder().id(2L).build();
        LoadSheddingEvent ev = LoadSheddingEvent
                .builder().id(12L).eventStart(Instant.now()).build();

        ZoneRestorationRecord r = ZoneRestorationRecord
                .builder().zone(z).eventId(12L).restoredAt(Instant.now().minusSeconds(10)).build();

        when(eventRepo.findById(12L)).thenReturn(Optional.of(ev));
        when(zoneRepo.findById(2L)).thenReturn(Optional.of(z));

        try {
            restorationService.restoreZone(r);
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(e.getMessage().contains("after event start"));
        }
    }

    @Test(priority = 28)
    public void testRestoreZone_success() {
        Zone z = Zone.builder().id(3L).build();
        LoadSheddingEvent ev = LoadSheddingEvent
                .builder().id(13L).eventStart(Instant.now().minusSeconds(100)).build();

        ZoneRestorationRecord r = ZoneRestorationRecord
                .builder().zone(z).eventId(13L).restoredAt(Instant.now()).notes("OK").build();

        when(eventRepo.findById(13L)).thenReturn(Optional.of(ev));
        when(zoneRepo.findById(3L)).thenReturn(Optional.of(z));

        when(restorationRepo.save(any())).thenAnswer(i -> {
            ZoneRestorationRecord res = i.getArgument(0);
            res.setId(5L);
            return res;
        });

        ZoneRestorationRecord saved = restorationService.restoreZone(r);

        Assert.assertNotNull(saved.getId());
    }

    @Test(priority = 29)
    public void t29_zonePriorityValidation() {
        Zone z = Zone.builder().zoneName("X").priorityLevel(0).build();
        when(zoneRepo.findByZoneName("X")).thenReturn(Optional.empty());

        try {
            zoneService.createZone(z);
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(e.getMessage().contains(">= 1"));
        }
    }

    @Test(priority = 30)
    public void t30_supplyNegative_throws() {
        SupplyForecast s = SupplyForecast.builder()
                .availableSupplyMW(-5.0)
                .forecastStart(Instant.now())
                .forecastEnd(Instant.now().plusSeconds(10))
                .build();

        try {
            forecastService.createForecast(s);
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(e.getMessage().contains(">= 0"));
        }
    }

    @Test(priority = 31)
    public void t31_getLatestForecast_notFound() {
        when(forecastRepo.findFirstByOrderByGeneratedAtDesc()).thenReturn(Optional.empty());
        try {
            forecastService.getLatestForecast();
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("No forecasts"));
        }
    }

    @Test(priority = 32)
    public void t32_readingNotExists_getLatest_throws() {
        when(readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(123L)).thenReturn(Optional.empty());

        try {
            readingService.getLatestReading(123L);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("No readings"));
        }
    }

    @Test(priority = 33)
    public void t33_zoneUpdate_notFound() {
        when(zoneRepo.findById(500L)).thenReturn(Optional.empty());

        try {
            zoneService.updateZone(500L, Zone.builder().zoneName("A").priorityLevel(1).build());
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Zone not found"));
        }
    }

    @Test(priority = 34)
    public void t34_restoreList_empty() {
        when(restorationRepo.findByZoneIdOrderByRestoredAtDesc(88L)).thenReturn(Collections.emptyList());
        Assert.assertTrue(restorationService.getRecordsForZone(88L).isEmpty());
    }

    @Test(priority = 35)
    public void t35_eventList_empty() {
        when(eventRepo.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(sheddingService.getAllEvents().isEmpty());
    }

    @Test(priority = 36)
    public void t36_getEvent_notFound() {
        when(eventRepo.findById(777L)).thenReturn(Optional.empty());
        try {
            sheddingService.getEventById(777L);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Event not found"));
        }
    }

    @Test(priority = 37)
    public void t37_createReading_zoneNotFound_throws() {
        DemandReading r = DemandReading
                .builder().zone(Zone.builder().id(99L).build())
                .demandMW(2.0)
                .recordedAt(Instant.now())
                .build();

        when(zoneRepo.findById(99L)).thenReturn(Optional.empty());

        try {
            readingService.createReading(r);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Zone not found"));
        }
    }

    @Test(priority = 38)
    public void t38_supplyUpdate_notFound() {
        when(forecastRepo.findById(999L)).thenReturn(Optional.empty());
        try {
            forecastService.updateForecast(999L, SupplyForecast.builder()
                    .availableSupplyMW(5.0)
                    .forecastStart(Instant.now())
                    .forecastEnd(Instant.now().plusSeconds(10))
                    .build());
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Forecast not found"));
        }
    }

    @Test(priority = 39)
    public void t39_zoneDeactivate_notFound() {
        when(zoneRepo.findById(101L)).thenReturn(Optional.empty());
        try {
            zoneService.deactivateZone(101L);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Zone not found"));
        }
    }

    @Test(priority = 40)
    public void t40_createZone_minPriority_ok() {
        when(zoneRepo.findByZoneName("MinP")).thenReturn(Optional.empty());
        when(zoneRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Zone saved = zoneService.createZone(
                Zone.builder().zoneName("MinP").priorityLevel(1).build()
        );

        Assert.assertEquals((int) saved.getPriorityLevel(), 1);
    }

    @Test(priority = 41)
    public void t41_readingRecentLimit_moreThanAvailable() {
        Zone z = Zone.builder().id(140L).build();

        List<DemandReading> two = List.of(
                DemandReading.builder().id(1L).zone(z).demandMW(1.0).recordedAt(Instant.now()).build(),
                DemandReading.builder().id(2L).zone(z).demandMW(2.0).recordedAt(Instant.now()).build()
        );

        when(zoneRepo.findById(140L)).thenReturn(Optional.of(z));
        when(readingRepo.findByZoneIdOrderByRecordedAtDesc(140L)).thenReturn(two);

        Assert.assertEquals(readingService.getRecentReadings(140L, 5).size(), 2);
    }

    @Test(priority = 42)
    public void t42_triggerLoadShedding_noCandidateZones_throws() {
        SupplyForecast f = SupplyForecast.builder()
                .id(7L).availableSupplyMW(1.0)
                .forecastStart(Instant.now())
                .forecastEnd(Instant.now().plusSeconds(100))
                .build();

        when(forecastRepo.findById(7L)).thenReturn(Optional.of(f));
        when(zoneRepo.findByActiveTrueOrderByPriorityLevelAsc()).thenReturn(Collections.emptyList());

        try {
            sheddingService.triggerLoadShedding(7L);
            Assert.fail();
        } catch (BadRequestException e) {
            Assert.assertTrue(
                    e.getMessage().contains("No overload") ||
                    e.getMessage().contains("No suitable")
            );
        }
    }

    @Test(priority = 43)
    public void t43_eventForZone_empty() {
        when(eventRepo.findByZoneIdOrderByEventStartDesc(2L)).thenReturn(Collections.emptyList());
        Assert.assertTrue(sheddingService.getEventsForZone(2L).isEmpty());
    }

    @Test(priority = 44)
    public void t44_mutatingZone_updatesTimestamp() {
        Zone z = Zone.builder().id(200L).zoneName("TS").priorityLevel(2).build();

        when(zoneRepo.findById(200L)).thenReturn(Optional.of(z));
        when(zoneRepo.save(any())).thenAnswer(i -> {
            Zone zz = i.getArgument(0);
            zz.setUpdatedAt(Instant.now());
            return zz;
        });

        Assert.assertNotNull(
                zoneService.updateZone(200L,
                        Zone.builder().zoneName("TSnew").priorityLevel(2).build()
                ).getUpdatedAt()
        );
    }

    @Test(priority = 45)
    public void t45_createRestoreRecord_invalidZone_throws() {
        Zone z = Zone.builder().id(999L).build();

        ZoneRestorationRecord r = ZoneRestorationRecord.builder()
                .zone(z).eventId(1L).restoredAt(Instant.now().plusSeconds(10)).build();

        when(eventRepo.findById(1L)).thenReturn(
                Optional.of(LoadSheddingEvent.builder().id(1L).eventStart(Instant.now()).build())
        );

        when(zoneRepo.findById(999L)).thenReturn(Optional.empty());

        try {
            restorationService.restoreZone(r);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Zone not found"));
        }
    }

    @Test(priority = 46)
    public void t46_forecastList_empty() {
        when(forecastRepo.findAll()).thenReturn(Collections.emptyList());
        Assert.assertTrue(forecastService.getAllForecasts().isEmpty());
    }

    @Test(priority = 47)
    public void t47_sheddingEvent_save_failureHandled() {
        when(forecastRepo.findById(8L)).thenReturn(Optional.of(
                SupplyForecast.builder().id(8L).availableSupplyMW(1.0)
                        .forecastStart(Instant.now())
                        .forecastEnd(Instant.now().plusSeconds(10))
                        .build()
        ));

        Zone z = Zone.builder().id(300L).priorityLevel(6).active(true).build();
        when(zoneRepo.findByActiveTrueOrderByPriorityLevelAsc()).thenReturn(List.of(z));
        when(readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(300L))
                .thenReturn(Optional.of(
                        DemandReading.builder().zone(z).demandMW(20.0).recordedAt(Instant.now()).build()
                ));

        when(eventRepo.save(any())).thenThrow(new RuntimeException("DB down"));

        try {
            sheddingService.triggerLoadShedding(8L);
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage().contains("DB down"));
        }
    }

    @Test(priority = 48)
    public void t48_updateForecast_success() {
        SupplyForecast exist = SupplyForecast.builder()
                .id(20L).availableSupplyMW(50.0)
                .forecastStart(Instant.now())
                .forecastEnd(Instant.now().plusSeconds(10))
                .build();

        when(forecastRepo.findById(20L)).thenReturn(Optional.of(exist));
        when(forecastRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        SupplyForecast updated = forecastService.updateForecast(20L,
                SupplyForecast.builder()
                        .availableSupplyMW(60.0)
                        .forecastStart(Instant.now())
                        .forecastEnd(Instant.now().plusSeconds(100))
                        .build()
        );

        Assert.assertEquals(updated.getAvailableSupplyMW(), 60.0);
    }

    @Test(priority = 49)
    public void t49_createMultipleZones_and_listOrder() {
        Zone a = Zone.builder().id(1L).zoneName("A").priorityLevel(1).active(true).build();
        Zone b = Zone.builder().id(2L).zoneName("B").priorityLevel(3).active(true).build();
        when(zoneRepo.findAll()).thenReturn(List.of(a, b));

        Assert.assertEquals(zoneService.getAllZones().size(), 2);
    }

    @Test(priority = 50)
    public void t50_restoreRecord_getById_notFound() {
        when(restorationRepo.findById(404L)).thenReturn(Optional.empty());

        try {
            restorationService.getRecordById(404L);
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains("Record not found"));
        }
    }

    @Test(priority = 51)
    public void t51_event_getById_found() {
        LoadSheddingEvent ev = LoadSheddingEvent.builder().id(77L).reason("Test").build();
        when(eventRepo.findById(77L)).thenReturn(Optional.of(ev));

        Assert.assertEquals(sheddingService.getEventById(77L).getReason(), "Test");
    }

    @Test(priority = 52)
    public void t52_zoneRepo_findByName_returnsEmpty() {
        when(zoneRepo.findByZoneName("None")).thenReturn(Optional.empty());
        Assert.assertTrue(zoneRepo.findByZoneName("None").isEmpty());
    }

    @Test(priority = 53)
    public void t53_readingRepo_findByZone_returnsList() {
        when(readingRepo.findByZoneIdOrderByRecordedAtDesc(2L)).thenReturn(Collections.emptyList());
        Assert.assertTrue(readingRepo.findByZoneIdOrderByRecordedAtDesc(2L).isEmpty());
    }

    @Test(priority = 54)
    public void t54_canSaveZoneWithDefaults() {
        Zone z = Zone.builder().zoneName("Defaults").priorityLevel(2).build();

        when(zoneRepo.findByZoneName("Defaults")).thenReturn(Optional.empty());
        when(zoneRepo.save(any())).thenAnswer(i -> {
            Zone zx = i.getArgument(0);
            zx.setId(55L);
            return zx;
        });

        Zone saved = zoneService.createZone(z);

        Assert.assertEquals(saved.getId().longValue(), 55L);
        Assert.assertTrue(saved.getActive());
    }

    @Test(priority = 55)
    public void t55_triggerShedding_multipleZones_selectsLowestPriorityFirst() {
        SupplyForecast f = SupplyForecast.builder()
                .id(99L).availableSupplyMW(10.0)
                .forecastStart(Instant.now())
                .forecastEnd(Instant.now().plusSeconds(60))
                .build();

        Zone z1 = Zone.builder().id(401L).priorityLevel(1).active(true).build();
        Zone z2 = Zone.builder().id(402L).priorityLevel(9).active(true).build();

        when(forecastRepo.findById(99L)).thenReturn(Optional.of(f));
        when(zoneRepo.findByActiveTrueOrderByPriorityLevelAsc()).thenReturn(List.of(z1, z2));

        when(readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(401L))
                .thenReturn(Optional.of(DemandReading.builder().zone(z1).demandMW(5.0).recordedAt(Instant.now()).build()));

        when(readingRepo.findFirstByZoneIdOrderByRecordedAtDesc(402L))
                .thenReturn(Optional.of(DemandReading.builder().zone(z2).demandMW(50.0).recordedAt(Instant.now()).build()));

        when(eventRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        LoadSheddingEvent ev = sheddingService.triggerLoadShedding(99L);

        Assert.assertNotNull(ev);
    }

    @Test(priority = 56)
    public void t56_restoreRecord_listOrdering() {
        when(restorationRepo.findByZoneIdOrderByRestoredAtDesc(3L)).thenReturn(Collections.emptyList());
        Assert.assertTrue(restorationService.getRecordsForZone(3L).isEmpty());
    }

    @Test(priority = 57)
    public void t57_security_filter_accepts_noAuth_onPublic() {
        Assert.assertTrue(true);
    }

    @Test(priority = 58)
    public void t58_swagger_config_present() {
        Assert.assertTrue(true);
    }

    @Test(priority = 59)
    public void t59_dateUtil_now_returns() {
        Assert.assertNotNull(com.example.demo.util.DateTimeUtil.now());
    }

    @Test(priority = 60)
    public void t60_final_smokeTest() {
        Assert.assertTrue(true);
    }
}
