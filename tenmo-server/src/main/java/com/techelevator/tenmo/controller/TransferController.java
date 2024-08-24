package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/transfers")
public class TransferController {


    private final TransferDao transferDao;


    @Autowired
    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }


    // Create a new transfer
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer createTransfer(@RequestBody Transfer transfer) {
        return transferDao.createTransfer(transfer);
    }


    // Get a specific transfer by ID
    @GetMapping("/{transferId}")
    public Transfer getTransferById(@PathVariable int transferId) {
        return transferDao.findById(transferId);
    }


    // Get all transfers by account ID
    @GetMapping("/account/{accountId}")
    public List<Transfer> getTransfersByAccountId(@PathVariable int accountId) {
        return transferDao.findAllTransfersByAccountId(accountId);
    }


    // Get all transfers by username
    @GetMapping("/user")
    public List<Transfer> getTransfersByUsername(Principal principal) {
        String username = principal.getName();
        return transferDao.findAllTransfersByUsername(username);
    }
}
