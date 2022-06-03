package com.cg.utils.exception;

public class TooHighTransactionException extends Exception {
    public TooHighTransactionException(String errorMessage) {
        super(errorMessage);
    }
}
