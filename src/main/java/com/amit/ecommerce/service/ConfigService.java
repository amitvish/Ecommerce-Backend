package com.amit.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    @Value("${external.api.user-url}")
    private String userApiUrl;

    @Value("${api.load-users.url}")
    private String loadUsersUrl;

    public String getUserApiUrl() {
        return userApiUrl;
    }

    public String getLoadUsersUrl() {
        return loadUsersUrl;
    }
}
