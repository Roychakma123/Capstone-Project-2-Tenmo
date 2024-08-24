package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/accounts")
public class AccountController {


    private final AccountDao accountDao;


    @Autowired
    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    // Get the balance for the authenticated user
    @GetMapping("/balance")
    public BigDecimal getBalance(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return accountDao.getBalanceByUsername(username);
    }


    // Get account info for the authenticated user
    @GetMapping("/info")
    public Account getAccountInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return accountDao.findByUsername(username);
    }


    // Get account by account ID
    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable int accountId) {
        return accountDao.findById(accountId);
    }


    // Get all accounts (admin access only)
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountDao.findAll();
    }


    // Transfer money between accounts
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer transferMoney(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Transfer transfer) {
        String username = userDetails.getUsername();
        return accountDao.transferMoney(username, transfer);
    }
}
