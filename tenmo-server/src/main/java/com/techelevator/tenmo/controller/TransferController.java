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


    
}
