package com.amit.ecommerce.service;

import com.amit.ecommerce.model.User;
import com.amit.ecommerce.repository.UserRepository;
import com.amit.ecommerce.dto.UserDTO;
import com.amit.ecommerce.dto.UserResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final ConfigService configService;

    public UserService(UserRepository userRepository, RestTemplate restTemplate, ConfigService configService) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.configService = configService;
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "fallBackLoadUsers")
    @Retry(name = "userService")
    public void loadUsersFromExternalApi() {
        logger.info("Attempting to fetch users from external API...");

        UserResponseDTO response = fetchUsers();

        if (response != null && response.getUsers() != null) {
            List<User> users = response.getUsers().stream().map(this::convertToEntity).toList();
            logger.info("Fetched {} users from external API. Saving to DB...", users.size());

            // userRepository.saveAll(users);
            for (User user : users) {
                userRepository.save(user);
            }
            logger.info("Users saved successfully in H2 database.");

        } else {
            logger.warn("Received empty response or null users list from external API.");
        }
    }

    public void fallBackLoadUsers(Throwable e) {
        logger.error("External API is down! Falling back to an empty dataset. Error: {}", e.getMessage());
    }

    private User convertToEntity(UserDTO userDTO) {
        logger.debug("Converting UserDTO to User entity: {}", userDTO);

        return new User(userDTO.getId(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getAge(),
                userDTO.getGender(), userDTO.getEmail(), userDTO.getPhone(), userDTO.getBirthDate(), userDTO.getImage(),
                userDTO.getRole(), userDTO.getSsn());
    }

    public UserResponseDTO fetchUsers() {
        String url = configService.getUserApiUrl();
        logger.info("Calling external API: {}", url);

        UserResponseDTO response = restTemplate.getForObject(url, UserResponseDTO.class);
        if (response != null) {
            logger.info("Received response from external API with {} users", response.getUsers().size());
        } else {
            logger.warn("Received null response from external API!");
        }
        return response;
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users...");

        List<User> users = userRepository.findAll();
        logger.info("Fetched {} users from database", users.size());
        return users;
    }

    public List<User> getUsersByRole(String role) {
        logger.info("Fetching users with role: {}", role);
        List<User> users = userRepository.findByRole(role);
        logger.info("Fetched {} users with role {}", users.size(), role);
        return users;
    }

    public List<User> getUsersSortedByAge(String order) {
        logger.info("Fetching users sorted by age in {} order", order);
        List<User> users = "desc".equalsIgnoreCase(order) ? userRepository.findAllByOrderByAgeDesc()
                : userRepository.findAllByOrderByAgeAsc();
        logger.info("Fetched {} users sorted by age", users.size());
        return users;
    }

    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        return userRepository.findById(id);
    }

    public Optional<User> getUserBySsn(String ssn) {
        logger.info("Fetching user with SSN: {}", ssn);
        return userRepository.findBySsn(ssn);
    }
}
