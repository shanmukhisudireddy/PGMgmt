package com.staysphere.usermgmt.dto;

public class UserUpdateRequest {
    private String fullName;

    private String emailId;

    public UserUpdateRequest(String fullName, String emailId){
        this.fullName = fullName;
        this.emailId = emailId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    
}
