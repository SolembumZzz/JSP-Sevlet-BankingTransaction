package com.cg.utils.exception;

public class TooLowTransactionException extends Exception {
    public TooLowTransactionException(String errorMessage) {
        super(errorMessage);
    }
}
