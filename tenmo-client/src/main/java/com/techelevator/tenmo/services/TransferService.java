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
        if (currentUserId == -1) {
            BasicLogger.log("Invalid user ID");
            return BigDecimal.ZERO;
        }

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(
                    API_BASE_URL + "accounts/balance/" + currentUserId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    BigDecimal.class
            );
            return response.getBody() != null ? response.getBody() : BigDecimal.ZERO;
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public List<User> getAllUsers() {
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(
                    API_BASE_URL + "users",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    User[].class
            );
            return response.getBody() != null ? Arrays.asList(response.getBody()) : null;
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }

    public boolean sendTransfer(int recipientUserId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            BasicLogger.log("Transfer amount must be greater than zero.");
            return false;
        }

        Transfer transfer = new Transfer();
        transfer.setUserFromId(currentUserId);
        transfer.setUserToId(recipientUserId);
        transfer.setAmount(amount);

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/send",
                    HttpMethod.POST,
                    makeAuthEntity(transfer),
                    Transfer.class
            );
            return response.getBody() != null && "Approved".equals(response.getBody().getStatus());
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

    public List<Transfer> viewTransfers() {
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    API_BASE_URL + "transfers",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            );
            return response.getBody() != null ? Arrays.asList(response.getBody()) : null;
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }

    public Transfer viewTransferDetails(Long transferId) {
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/" + transferId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }

    public boolean requestTransfer(int senderUserId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            BasicLogger.log("Requested amount must be greater than zero.");
            return false;
        }

        Transfer transfer = new Transfer();
        transfer.setUserFromId(senderUserId);
        transfer.setUserToId(currentUserId);
        transfer.setAmount(amount);

        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/request",
                    HttpMethod.POST,
                    makeAuthEntity(transfer),
                    Transfer.class
            );
            return response.getBody() != null && "Pending".equals(response.getBody().getStatus());
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
            return false;
        }
    }

    public List<Transfer> viewPendingTransfers() {
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    API_BASE_URL + "transfers/pending",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            );
            return response.getBody() != null ? Arrays.asList(response.getBody()) : null;
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }

    public boolean approveTransfer(Long transferId) {
        return updateTransferStatus(transferId, "Approved");
    }

    public boolean rejectTransfer(Long transferId) {
        return updateTransferStatus(transferId, "Rejected");
    }

    private boolean updateTransferStatus(Long transferId, String status) {
        try {
            Transfer transfer = viewTransferDetails(transferId);
            if (transfer != null) {
                transfer.setStatus(status);
                restTemplate.exchange(
                        API_BASE_URL + "transfers/" + transferId,
                        HttpMethod.PUT,
                        makeAuthEntity(transfer),
                        Transfer.class
                );
                return true;
            }
        } catch (RestClientException e) {
            BasicLogger.log(e.getMessage());
        }
        return false;


    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private <T> HttpEntity<T> makeAuthEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(body, headers);
    }
}
