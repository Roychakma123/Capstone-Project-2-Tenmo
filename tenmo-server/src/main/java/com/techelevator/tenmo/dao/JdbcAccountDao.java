package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class JdbcAccountDao implements AccountDao {


    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getBalanceByUsername(String username) {
        String sql = "SELECT balance FROM account WHERE user_id = (SELECT id FROM users WHERE username = ?)";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
    }


    @Override
    public Account findByUsername(String username) {
        String sql = "SELECT * FROM account WHERE user_id = (SELECT id FROM users WHERE username = ?)";
        return jdbcTemplate.queryForObject(sql, new AccountRowMapper(), username);
    }


    @Override
    public Account findById(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, new AccountRowMapper(), accountId);
    }


    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM account";
        return jdbcTemplate.query(sql, new AccountRowMapper());
    }


    @Override
    public Transfer transferMoney(String username, Transfer transfer) {

        Account accountFrom = findByUsername(username);
        Account accountTo = findById(transfer.getAccountTo());

        BigDecimal newBalanceFrom = accountFrom.getBalance().subtract(transfer.getAmount());
        BigDecimal newBalanceTo = accountTo.getBalance().add(transfer.getAmount());

        updateBalance(accountFrom.getAccountId(), newBalanceFrom);
        updateBalance(accountTo.getAccountId(), newBalanceTo);

        String sql = "INSERT INTO transfer (account_from, account_to, amount) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());


        return transfer;
    }


    private void updateBalance(int accountId, BigDecimal newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);
    }


    private static class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            Account account = new Account();
            account.setAccountId(rs.getInt("account_id"));
            account.setUserId(rs.getInt("user_id"));
            account.setBalance(rs.getBigDecimal("balance"));
            return account;
        }
    }
}
