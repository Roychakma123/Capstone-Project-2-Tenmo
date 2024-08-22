package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private static final String API_BASE_URL = "http://localhost:8080/"; // Adjust if necessary
    private final RestTemplate restTemplate;
    private final String authToken;
    private final int currentUserId;

    public TransferService(AuthenticatedUser currentUser) {
        this.restTemplate = new RestTemplate();
        this.authToken = currentUser != null ? currentUser.getToken() : null;
        this.currentUserId = currentUser != null && currentUser.getUser() != null ? currentUser.getUser().getId() : -1;
    }

    public BigDecimal getAccountBalance() {
        return null;
    }
}