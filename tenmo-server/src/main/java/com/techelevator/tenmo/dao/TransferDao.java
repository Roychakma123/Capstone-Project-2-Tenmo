package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfer;


import java.util.List;


public interface TransferDao {


    Transfer createTransfer(Transfer transfer);
    Transfer getTransferById(int transferId);



    Transfer findById(int transferId);


    List<Transfer> findAllTransfersByAccountId(int accountId);


    List<Transfer> findAllTransfersByUsername(String username);
}
