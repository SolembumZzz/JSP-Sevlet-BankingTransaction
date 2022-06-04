package com.cg.service;

import com.cg.model.Transfer;
import com.cg.utils.MySQLConnectionUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransferService implements ITransferService {
    private static final String VIEW_TRANSFER_HISTORY_SQL =
            "SELECT id, sender_id, sender_name, recipient_id, recipient_name, transfer_amount, fee_rate, fee " +
            "FROM banking_transaction.vw_transfer;";

    private static final String VIEW_TRANSFER_FEES_SQL =
            "SELECT fee " +
            "FROM banking_transaction.vw_transfer;";

    @Override
    public List<Transfer> selectAllTransfers() {
        List<Transfer> allTransfers = new ArrayList<>();
        try {
            Connection conn = MySQLConnectionUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(VIEW_TRANSFER_HISTORY_SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int senderId = rs.getInt("sender_id");
                String senderName = rs.getString("sender_name");
                int recipientId = rs.getInt("recipient_id");
                String recipientName = rs.getString("recipient_name");
                long transfer_amount = rs.getLong("transfer_amount");
                int feeRate = rs.getInt("fee_rate");
                long fee = rs.getLong("fee");

                allTransfers.add(new Transfer(id, senderId, senderName, recipientId, recipientName,
                        transfer_amount, feeRate, fee));
            }
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        return allTransfers;
    }

    @Override
    public BigDecimal calculateTotalFees() {
        BigDecimal totalFees = BigDecimal.ZERO;
        try {
            Connection conn = MySQLConnectionUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(VIEW_TRANSFER_HISTORY_SQL);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                totalFees = totalFees.add(rs.getBigDecimal("fee"));
            }
        } catch (SQLException e) {
            MySQLConnectionUtils.printSQLException(e);
        }
        return totalFees;
    }
}
