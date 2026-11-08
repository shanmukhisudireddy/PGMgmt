package com.staysphere.usermgmt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationRequest {
    @NotBlank (message = "Name can not be blank")
    private String fullName;

    @NotBlank (message = "Email Id can not be blank")
    @Email 
    private String emailId;

    @NotBlank (message = "Password is expected")
    @Size (min = 8, message = "Minimum 8 characters required")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



}
