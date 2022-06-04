package com.cg.utils.exception;

public class SuspendedCustomerException extends Exception {
    public SuspendedCustomerException(String errorMessage) {
        super(errorMessage);
    }
}
