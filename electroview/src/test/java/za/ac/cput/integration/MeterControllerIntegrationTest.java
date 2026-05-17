package za.ac.cput.integration;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import za.ac.cput.domain.Meter;
import za.ac.cput.domain.Zone;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeterControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static String createdMeterId;
    private static String zoneId;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void setUpPatchSupport() {
        restTemplate.getRestTemplate().setRequestFactory(
                new HttpComponentsClientHttpRequestFactory(
                        HttpClients.createDefault()));
    }

    @Test
    @Order(1)
    void setUp_CreateZoneForMeter() {
        String path = "/api/zones?name=Meter Test Zone"
                    + "&description=Zone for meter tests"
                    + "&location=Western Cape"
                    + "&capacityKwh=8000.0";

        ResponseEntity<Zone> response =
                restTemplate.postForEntity(url(path), null, Zone.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        zoneId = response.getBody().getZoneId();
    }

    @Test
    @Order(2)
    void registerMeter_ReturnsCreated() {
        String path = "/api/meters?serialNumber=SN-INT-001"
                    + "&zoneId=" + zoneId;

        ResponseEntity<Meter> response =
                restTemplate.postForEntity(url(path), null, Meter.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("SN-INT-001", response.getBody().getSerialNumber());

        createdMeterId = response.getBody().getMeterId();
    }

    @Test
    @Order(3)
    void getMeterById_ReturnsMeter() {
        ResponseEntity<Meter> response = restTemplate.getForEntity(
                url("/api/meters/" + createdMeterId), Meter.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdMeterId, response.getBody().getMeterId());
    }

    @Test
    @Order(4)
    void activateMeter_ReturnsOk() {
        ResponseEntity<Meter> response = restTemplate.exchange(
                url("/api/meters/" + createdMeterId + "/activate"),
                HttpMethod.PATCH,
                null,
                Meter.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    void getMetersByZone_ReturnsListContainingMeter() {
        ResponseEntity<Meter[]> response = restTemplate.getForEntity(
                url("/api/meters/zone/" + zoneId), Meter[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 1);
    }

    @Test
    @Order(6)
    void assignConsumer_ReturnsUpdatedMeter() {
        ResponseEntity<Meter> response = restTemplate.exchange(
                url("/api/meters/" + createdMeterId
                        + "/consumer?consumerId=consumer-int-1"),
                HttpMethod.PATCH,
                null,
                Meter.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("consumer-int-1", response.getBody().getConsumerId());
    }
}