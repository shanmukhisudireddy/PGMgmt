package com.staysphere.usermgmt.entity;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Table(
    name = "permissions",
    uniqueConstraints = {@UniqueConstraint (name = "uk_permission_name", columnNames = "permission_name")}
)

@Entity 
public class Permission {

    @Column (name = "permission_name", nullable = false)
    private String permissionName;

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long permissionId;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    public Permission(String permissionName){
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    
    public Permission(){}
    

}
