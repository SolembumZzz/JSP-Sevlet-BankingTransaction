package com.cg.service;

import com.cg.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface ITransferService {
    public List<Transfer> selectAllTransfers();

    public BigDecimal calculateTotalFees();
}
