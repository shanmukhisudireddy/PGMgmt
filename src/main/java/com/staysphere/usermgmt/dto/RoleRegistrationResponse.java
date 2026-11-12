package com.staysphere.usermgmt.dto;

import java.util.List;

public class RoleRegistrationResponse {
    public String emailId;

    public String fullName;

    public List<RoleResponse> roles;

    public RoleRegistrationResponse(String emailId, String fullName, List<RoleResponse> roles){
        this.emailId = emailId;
        this.fullName = fullName;
        this.roles = roles;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getFullName() {
        return fullName;
    }

    public List<RoleResponse> getRoles() {
        return roles;
    }
    

}
