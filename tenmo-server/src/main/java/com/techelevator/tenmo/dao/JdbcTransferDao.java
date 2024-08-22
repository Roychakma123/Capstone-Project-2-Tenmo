package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


//public class JdbcTransferDao implements TransferDao {
  //  return null;

    /*
    // RowMapper for Transfer class
    private static class TransferRowMapper implements RowMapper<Transfer> {
        @Override
        public Transfer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transfer transfer = new Transfer();
            transfer.setTransferId(rs.getInt("transfer_id"));
            transfer.setAccountFrom(rs.getInt("account_from"));
            transfer.setAccountTo(rs.getInt("account_to"));
            transfer.setAmount(rs.getBigDecimal("amount"));
            transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
            transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
            return transfer;
        }
    }
*/