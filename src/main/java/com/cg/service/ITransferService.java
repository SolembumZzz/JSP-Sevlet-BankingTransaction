package com.cg.service;

import com.cg.model.Transfer;

import java.util.List;

public interface ITransferService {
    public List<Transfer> selectAllTransfers();

    public long calculateTotalFees();
}
