package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcTransferDao implements TransferDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {
        Transfer newTransfer =null;
        String sql = "INSERT into transfer(transfer_type_id, transfer_status_id, acoount_from, account_to, amount) " +
                "values (?,?,?,?,?) " +
                "Returning transfer_id";
        try {
            int newTransferId= jdbcTemplate.queryForObject(sql, int.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
            if (newTransferId>0) {
                newTransfer = getTransferById(newTransferId);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfer;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
            if (rowSet.next()) {
                transfer = mapRow(rowSet);
            }
        } catch (CannotGetJdbcConnectionException | SQLException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }

    // RowMapper for Transfer class


        public Transfer mapRow(SqlRowSet rs) throws SQLException {
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
