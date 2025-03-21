package com.amit.ecommerce.service;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StartupService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ConfigService configService;

    public StartupService(ConfigService configService) {
        this.configService = configService;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        String url = configService.getLoadUsersUrl();
        try {
            restTemplate.postForObject(url, null, String.class);
            System.out.println("User data loaded successfully!");
        } catch (Exception e) {
            System.err.println("Failed to load user data: " + e.getMessage());
        }
    }
}
