package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;


import java.math.BigDecimal;
import java.util.List;


public interface AccountDao {


    BigDecimal getBalanceByUsername(String username);
    Account findByUsername(String username);
    Account findById(int accountId);
    List<Account> findAll();
    Transfer transferMoney(String username, Transfer transfer);
}
