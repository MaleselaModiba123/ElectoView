package za.ac.cput.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private final ObjectMapper mapper = new ObjectMapper();

    private static String createdUserId;

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
    void createUser_ReturnsCreated() throws Exception {
        String path = "/api/users?name=Malesela Modiba"
                    + "&email=integration@cput.ac.za"
                    + "&passwordHash=hashed123"
                    + "&role=ANALYST";

        ResponseEntity<String> response =
                restTemplate.postForEntity(url(path), null, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        JsonNode body = mapper.readTree(response.getBody());
        assertEquals("integration@cput.ac.za", body.get("email").asText());

        createdUserId = body.get("id").asText();
        assertNotNull(createdUserId);
    }

    @Test
    @Order(2)
    void createUser_DuplicateEmail_ReturnsBadRequest() {
        String path = "/api/users?name=Duplicate"
                    + "&email=integration@cput.ac.za"
                    + "&passwordHash=hashed123"
                    + "&role=CONSUMER";

        ResponseEntity<String> response =
                restTemplate.postForEntity(url(path), null, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(3)
    void getUserById_ReturnsUser() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                url("/api/users/" + createdUserId), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = mapper.readTree(response.getBody());
        assertEquals(createdUserId, body.get("id").asText());
    }

    @Test
    @Order(4)
    void getAllUsers_ReturnsListContainingCreatedUser() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                url("/api/users"), String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = mapper.readTree(response.getBody());
        assertTrue(body.isArray());
        assertTrue(body.size() >= 1);
    }

    @Test
    @Order(5)
    void activateUser_ReturnsActiveStatus() throws Exception {
        ResponseEntity<String> response = restTemplate.exchange(
                url("/api/users/" + createdUserId + "/activate"),
                HttpMethod.PATCH,
                null,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        JsonNode body = mapper.readTree(response.getBody());
        assertEquals("ACTIVE", body.get("status").asText());
    }
}