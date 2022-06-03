package com.cg.utils.exception;

public class ExistingEmailException extends Exception{

    public ExistingEmailException (String errorMessage){
        super(errorMessage);
    }
}
