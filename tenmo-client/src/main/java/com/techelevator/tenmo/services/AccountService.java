package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_BASE_URL = "http://localhost:8080/"; //double-check host
    private String authenticationToken;

    public AccountService(AuthenticatedUser currentUser) {
        if (currentUser != null) {
            this.authenticationToken = currentUser.getToken();
        }
    }

    public BigDecimal getCurrentBalance(int accountId) {
        BigDecimal balance = BigDecimal.ZERO;

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(
                    API_BASE_URL + "accounts/balance/" + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    BigDecimal.class
            );
            if (response.getBody() != null) {
                balance = response.getBody();
            }
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
        }

        return balance;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticationToken);
        return new HttpEntity<>(headers);
    }
}