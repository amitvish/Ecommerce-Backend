package com.amit.ecommerce.controller;

import com.amit.ecommerce.model.User;
import com.amit.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/load")
    public String loadUsers() {
        logger.info("Request received: Loading users from external API.");

        userService.loadUsersFromExternalApi();
        logger.info("Users successfully loaded into H2 database.");

        return "user loaded successfully";
    }

    @Operation(summary = "Get all users", description = "Fetches a list of all available users")
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Request received: Fetching all users.");

        List<User> users = userService.getAllUsers();
        logger.info("Successfully fetched {} users.", users.size());

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users by role", description = "Fetches users based on their role (e.g., ADMIN, CUSTOMER)")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        logger.info("Request received : Fetching users by role.");

        List<User> users = userService.getUsersByRole(role);
        logger.info("Successfully fetched {} users by role", users.size());

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get users sorted by age", description = "Fetches users sorted by age in ascending or descending order")
    @GetMapping("/sorted")
    public ResponseEntity<List<User>> getUsersSorted(@RequestParam(defaultValue = "asc") String order) {
        logger.info("Request received: Fetching users sorted by age in {} order.", order);

        List<User> users = userService.getUsersSortedByAge(order);
        logger.info("Successfully fetched {} users sorted by age in {} order.", users.size(), order);

        return ResponseEntity.ok(userService.getUsersSortedByAge(order));
    }

    @Operation(summary = "Find user by ID", description = "Fetches a specific user by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Request received: Fetching user with ID: {}", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            logger.info("User found by id: {}", user.get());
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("User not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Find user by SSN", description = "Fetches a specific user by their SSN")
    @GetMapping("/ssn/{ssn}")
    public ResponseEntity<User> getUserBySsn(@PathVariable String ssn) {
        logger.info("Request received: Fetching user with SSN: {}", ssn);
        Optional<User> user = userService.getUserBySsn(ssn);
        if (user.isPresent()) {
            logger.info("User found by ssn: {}", user.get());

            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("User not found with SSN: {}", ssn);

            return ResponseEntity.notFound().build();
        }
    }
}
