package com.amit.ecommerce.controller;

import com.amit.ecommerce.model.User;
import com.amit.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/users";
        userRepository.save(new User(1L, "John", "Doe", 30, "Male", "john.doe@example.com", "+1234567890", "1993-05-15",
                "ADMIN", "123-45-6789"));
        userRepository.save(new User(2L, "Jane", "Smith", 25, "Female", "jane.smith@example.com", "+9876543210",
                "1998-10-20", "CUSTOMER", "987-65-4321"));
    }

    @Test
    void testGetAllUsers() {
        ResponseEntity<List<User>> response = restTemplate.exchange(baseUrl + "/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetUserByRole() {
        ResponseEntity<List<User>> response = restTemplate.exchange(baseUrl + "/role/ADMIN", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("ADMIN", response.getBody().getFirst().getRole());
    }

    @Test
    void testGetUsersSortedByAgeAscending() {
        ResponseEntity<List<User>> response = restTemplate.exchange(baseUrl + "/sorted?order=asc", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(25, response.getBody().getFirst().getAge());
        assertEquals(30, response.getBody().get(1).getAge());
    }

    @Test
    void testGetUserById_Found() {
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/1", User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    void testGetUserById_NotFound() {
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/99", User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetUserBySsn_Found() {
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/ssn/123-45-6789", User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    void testGetUserBySsn_NotFound() {
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/ssn/999-99-9999", User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testLoadUsers() {
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/load", null, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user loaded successfully", response.getBody());
    }
}
