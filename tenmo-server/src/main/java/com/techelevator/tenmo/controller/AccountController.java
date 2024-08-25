package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/accounts")
@PreAuthorize("isAuthenticated")
public class AccountController {

    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @Autowired
    TransferDao transferDao;



    // Get the balance for the authenticated user
    @GetMapping("/balance")
    public BigDecimal getBalance(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return accountDao.getBalanceByUsername(username);
    }


//    // Get account info for the authenticated user
//    @GetMapping("/info")
//    public Account getAccountInfo(@AuthenticationPrincipal UserDetails userDetails) {
//        String username = userDetails.getUsername();
//        return accountDao.getBalanceByUsername(username);
//    }
//
//
//    // Get account by account ID
//    @GetMapping("/{accountId}")
//    public Account getAccountById(@PathVariable int accountId) {
//        return accountDao.findById(accountId);
//    }
//
//


//
//    // Transfer money between accounts
//    @PostMapping("/transfer")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Transfer transferMoney(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Transfer transfer) {
//        String username = userDetails.getUsername();
//        return accountDao.transferMoney(username, transfer);
//    }
}
