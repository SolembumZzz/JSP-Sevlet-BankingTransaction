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
    int fees;
    long feeAmount;
    long transacAmt;
    long transferAmt;
    int recipientId;
    int senderId;

    public Transfer() {
        this.fees = 10;
    }

    public Transfer(int id, LocalDate createdAt, BigInteger createdBy, boolean deleted, LocalDate updatedAt,
                    BigInteger updatedBy, long feeAmount, long transacAmt, long transferAmt,
                    int recipientId, int senderId) {
        this.id = id;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.deleted = deleted;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.fees = 10;
        this.feeAmount = feeAmount;
        this.transacAmt = transacAmt;
        this.transferAmt = transferAmt;
        this.recipientId = recipientId;
        this.senderId = senderId;
    }

    public Transfer(int id, int recipientId, int senderId, long transferAmt, long transacAmt) {
        this.id = id;
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.transacAmt = transacAmt;
        this.transferAmt = transferAmt;
        this.fees = 10;
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

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
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
}
