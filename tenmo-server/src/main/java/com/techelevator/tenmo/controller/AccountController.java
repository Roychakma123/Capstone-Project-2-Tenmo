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



}
