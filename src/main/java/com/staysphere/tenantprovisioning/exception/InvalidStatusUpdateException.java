package com.staysphere.tenantprovisioning.exception;

public class InvalidStatusUpdateException extends RuntimeException{
    public InvalidStatusUpdateException(String msg){
        super(msg);
    }
}
