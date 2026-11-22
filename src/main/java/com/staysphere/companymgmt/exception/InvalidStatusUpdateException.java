package com.staysphere.companymgmt.exception;

public class InvalidStatusUpdateException extends RuntimeException{
    public InvalidStatusUpdateException(String msg){
        super(msg);
    }
}
