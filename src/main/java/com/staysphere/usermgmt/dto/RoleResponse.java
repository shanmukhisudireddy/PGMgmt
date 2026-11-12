package com.staysphere.usermgmt.dto;

import java.util.List;

public class RoleResponse {

    private Long roleId;
    private String roleName;
    private List<String> permissions;

    public RoleResponse(Long roleId, String roleName, List<String> permissions) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.permissions = permissions;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}