package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalanceByUsername(String username) {
        BigDecimal balance = null;
        String sql = "select balance from account \n" +
                "join tenmo_user using(user_id)\n" +
                "where tenmo_user.username=?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
            if(results.next()){
                balance = results.getBigDecimal("balance");

            }
        }catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to a database", e);
        }
        return  balance;
    }
}