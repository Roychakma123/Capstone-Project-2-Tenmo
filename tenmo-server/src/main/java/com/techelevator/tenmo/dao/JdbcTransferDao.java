package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
public class JdbcTransferDao implements TransferDao {


    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (account_from, account_to, amount, transfer_type_id, transfer_status_id) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount(),
                transfer.getTransferTypeId(), transfer.getTransferStatusId());
        transfer.setTransferId(newId);
        return transfer;
    }


    @Override
    public Transfer findById(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        return jdbcTemplate.queryForObject(sql, new TransferRowMapper(), transferId);
    }


    @Override
    public List<Transfer> findAllTransfersByAccountId(int accountId) {
        String sql = "SELECT * FROM transfer WHERE account_from = ? OR account_to = ?";
        return jdbcTemplate.query(sql, new TransferRowMapper(), accountId, accountId);
    }


    @Override
    public List<Transfer> findAllTransfersByUsername(String username) {
        String sql = "SELECT t.* FROM transfer t " +
                "JOIN account a ON t.account_from = a.account_id OR t.account_to = a.account_id " +
                "JOIN users u ON a.user_id = u.id WHERE u.username = ?";
        return jdbcTemplate.query(sql, new TransferRowMapper(), username);
    }


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
}
