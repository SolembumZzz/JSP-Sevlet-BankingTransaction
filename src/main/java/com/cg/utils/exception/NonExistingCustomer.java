package com.cg.utils.exception;

public class NonExistingCustomer extends Exception {
    public NonExistingCustomer(String errorMessage) {
        super(errorMessage);
    }
}
