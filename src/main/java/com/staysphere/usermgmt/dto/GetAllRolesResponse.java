package com.staysphere.usermgmt.dto;

import java.util.List;

public class GetAllRolesResponse {
    public List<RoleResponse> roles;

    public GetAllRolesResponse(List<RoleResponse> roles){
        this.roles = roles;
    }

    public List<RoleResponse> getRoles() {
        return roles;
    }

}
