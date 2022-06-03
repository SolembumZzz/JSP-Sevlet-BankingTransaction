package com.cg.utils.exception;

public class TransactionIncompletedException extends Exception {
    public TransactionIncompletedException(String errorMessage) {
        super(errorMessage);
    };
}
