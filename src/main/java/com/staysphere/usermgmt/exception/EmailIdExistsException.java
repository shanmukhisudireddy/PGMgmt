package com.staysphere.usermgmt.exception;

public class EmailIdExistsException extends RuntimeException{

    public EmailIdExistsException(String msg){
        super(msg);
    }

}
