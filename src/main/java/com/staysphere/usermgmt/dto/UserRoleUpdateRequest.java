package com.staysphere.usermgmt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class UserRoleUpdateRequest {
    @NotBlank 
    public String fullName;
    
    @NotBlank 
    @Email 
    public String emailId;

    @NotEmpty (message = "Atleast one role is required")
    public Set<@NotBlank String> roles;

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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }



}
