package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class UserService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_BASE_URL = "http://localhost:8080/user"; //double-check host
    private String authenticationToken;

    public UserService(AuthenticatedUser currentUser) {
        if (currentUser != null) {
            this.authenticationToken = currentUser.getToken();
        }
    }
    public User[] getUsers(){
        User[] users = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(
                    API_BASE_URL,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    User[].class
            );
            if (response.getBody() != null) {
                users= response.getBody();
            }
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
        }

        return users;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticationToken);
        return new HttpEntity<>(headers);
    }
}
