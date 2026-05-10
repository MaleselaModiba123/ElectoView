package za.ac.cput;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.ac.cput.domain.*;
import za.ac.cput.Enums.*;
import za.ac.cput.repository.MeterRepository;
import za.ac.cput.service.MeterService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeterServiceTest {

    @Mock
    private MeterRepository meterRepository;

    @InjectMocks
    private MeterService meterService;

    private Meter testMeter;

    @BeforeEach
    void setUp() {
        testMeter = Meter.builder()
                .serialNumber("SN-BV-001")
                .zoneId("zone-1")
                .build();
    }

    @Test
    void registerMeter_Success() {
        when(meterRepository.existsBySerialNumber("SN-BV-001"))
                .thenReturn(false);
        when(meterRepository.save(any(Meter.class)))
                .thenReturn(testMeter);

        Meter result = meterService.registerMeter("SN-BV-001", "zone-1");

        assertNotNull(result);
        assertEquals("SN-BV-001", result.getSerialNumber());
        verify(meterRepository, times(1)).save(any(Meter.class));
    }

    @Test
    void registerMeter_DuplicateSerial_ThrowsException() {
        when(meterRepository.existsBySerialNumber("SN-BV-001"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                meterService.registerMeter("SN-BV-001", "zone-1")
        );

        verify(meterRepository, never()).save(any(Meter.class));
    }

    @Test
    void activate_SetsStatusToActive() {
        when(meterRepository.findById("meter-1"))
                .thenReturn(Optional.of(testMeter));
        when(meterRepository.save(any(Meter.class)))
                .thenReturn(testMeter);

        Meter result = meterService.activate("meter-1");

        assertEquals(MeterStatus.ACTIVE, result.getStatus());
    }

    @Test
    void decommission_SetsStatusToDecommissioned() {
        when(meterRepository.findById("meter-1"))
                .thenReturn(Optional.of(testMeter));
        when(meterRepository.save(any(Meter.class)))
                .thenReturn(testMeter);

        Meter result = meterService.decommission("meter-1");

        assertEquals(MeterStatus.DECOMMISSIONED, result.getStatus());
    }

    @Test
    void assignConsumer_LinksConsumerToMeter() {
        when(meterRepository.findById("meter-1"))
                .thenReturn(Optional.of(testMeter));
        when(meterRepository.save(any(Meter.class)))
                .thenReturn(testMeter);

        Meter result = meterService.assignConsumer("meter-1", "consumer-1");

        assertEquals("consumer-1", result.getConsumerId());
    }

    @Test
    void findByZone_ReturnsMeterList() {
        when(meterRepository.findByZoneId("zone-1"))
                .thenReturn(List.of(testMeter));

        List<Meter> result = meterService.findByZone("zone-1");

        assertEquals(1, result.size());
        assertEquals("SN-BV-001", result.get(0).getSerialNumber());
    }
}