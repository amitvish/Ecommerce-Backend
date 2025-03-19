package com.amit.ecommerce.service;

import com.amit.ecommerce.model.User;
import com.amit.ecommerce.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Ensures DB is reset after each test
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService; // No mocking!

    @BeforeEach
    void setUp() {
        userRepository.save(new User(1L, "John", "Doe", 30, "Male", "john.doe@example.com", "+1234567890", "1993-05-15",
                "ADMIN", "123-45-6789"));
        userRepository.save(new User(2L, "Jane", "Smith", 25, "Female", "jane.smith@example.com", "+9876543210",
                "1998-10-20", "CUSTOMER", "987-65-4321"));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testGetUsersByRole() {
        List<User> admins = userService.getUsersByRole("ADMIN");
        assertEquals(1, admins.size());
        assertEquals("John", admins.getFirst().getFirstName());
    }

    @Test
    void testGetUserById() {
        User user = userService.getUserById(1L).orElse(null);
        assertNotNull(user);
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testGetUserBySsn() {
        User user = userService.getUserBySsn("987-65-4321").orElse(null);
        assertNotNull(user);
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    void testGetUsersSortedByAge() {
        List<User> usersAsc = userService.getUsersSortedByAge("asc");
        assertEquals("Jane", usersAsc.getFirst().getFirstName()); // Younger user comes first

        List<User> usersDesc = userService.getUsersSortedByAge("desc");
        assertEquals("John", usersDesc.getFirst().getFirstName()); // Older user comes first
    }
}
