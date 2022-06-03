package com.cg.model;

import java.math.BigInteger;
import java.time.LocalDate;

public class Transfer {
    int id;
    LocalDate createdAt;
    BigInteger createdBy;
    boolean deleted;
    LocalDate updatedAt;
    BigInteger updatedBy;
    int feeRate;
    long feeAmount;
    long transacAmt;
    long transferAmt;
    int recipientId;
    String recipientName;
    int senderId;
    String senderName;

    public Transfer() {
        this.feeRate = 10;
    }

    public Transfer(int id, LocalDate createdAt, BigInteger createdBy, boolean deleted, LocalDate updatedAt, BigInteger updatedBy, int feeRate, long feeAmount, long transacAmt, long transferAmt, int recipientId, String recipientName, int senderId, String senderName) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.deleted = deleted;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.feeRate = feeRate;
        this.feeAmount = feeAmount;
        this.transacAmt = transacAmt;
        this.transferAmt = transferAmt;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.senderId = senderId;
        this.senderName = senderName;
    }

    public Transfer(int id, int recipientId, int senderId, long transferAmt, long transacAmt) {
        this.id = id;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.transacAmt = transacAmt;
        this.transferAmt = transferAmt;
        this.feeRate = 10;
    }

    public Transfer(int id, int senderId, String senderName, int recipientId, String recipientName,
                    long transfer_amount, int feeRate, long fee) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
        this.transferAmt = transfer_amount;
        this.feeRate = feeRate;
        this.feeAmount = fee;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public BigInteger getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(BigInteger createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BigInteger getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(BigInteger updatedBy) {
        this.updatedBy = updatedBy;
    }

    public int getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(int feeRate) {
        this.feeRate = feeRate;
    }

    public long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public long getTransacAmt() {
        return transacAmt;
    }

    public void setTransacAmt(long transacAmt) {
        this.transacAmt = transacAmt;
    }

    public long getTransferAmt() {
        return transferAmt;
    }

    public void setTransferAmt(long transferAmt) {
        this.transferAmt = transferAmt;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
