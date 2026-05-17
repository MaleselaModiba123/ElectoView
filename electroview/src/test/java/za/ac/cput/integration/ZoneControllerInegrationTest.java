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
import za.ac.cput.domain.Zone;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ZoneControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private static String createdZoneId;

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
    void createZone_ReturnsCreated() {
        String path = "/api/zones?name=Integration Zone"
                    + "&description=Test zone"
                    + "&location=Western Cape"
                    + "&capacityKwh=5000.0";

        ResponseEntity<Zone> response =
                restTemplate.postForEntity(url(path), null, Zone.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Integration Zone", response.getBody().getName());

        createdZoneId = response.getBody().getZoneId();
    }

    @Test
    @Order(2)
    void getZoneById_ReturnsZone() {
        ResponseEntity<Zone> response = restTemplate.getForEntity(
                url("/api/zones/" + createdZoneId), Zone.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdZoneId, response.getBody().getZoneId());
    }

    @Test
    @Order(3)
    void getAllZones_ReturnsList() {
        ResponseEntity<Zone[]> response = restTemplate.getForEntity(
                url("/api/zones"), Zone[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 1);
    }

    @Test
    @Order(4)
    void activateZone_ReturnsOk() {
        ResponseEntity<Zone> response = restTemplate.exchange(
                url("/api/zones/" + createdZoneId + "/activate"),
                HttpMethod.PATCH,
                null,
                Zone.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    void updateThreshold_ReturnsUpdatedZone() {
        ResponseEntity<Zone> response = restTemplate.exchange(
                url("/api/zones/" + createdZoneId
                        + "/threshold?type=ABSOLUTE&value=4500.0"),
                HttpMethod.PATCH,
                null,
                Zone.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4500.0, response.getBody().getThresholdValue());
    }
}