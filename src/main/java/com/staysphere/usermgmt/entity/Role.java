package com.staysphere.usermgmt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;

import java.util.HashSet;
import java.util.Set;
@Table (
    name = "roles",
    uniqueConstraints = {@UniqueConstraint (name = "uk_role_name", columnNames = "role_name")}
)



@Entity 
public class Role {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long roleId;
    
    @Column (name = "role_name", nullable = false)
    private String roleName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    @ManyToMany
    @JoinTable(
    name = "role_permissions",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn (name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public Role(){}

    public Role(String roleName){
        this.roleName = roleName;
    }

    public Role addPermission(Permission permission){
        if(permission == null){
            throw new IllegalArgumentException();
        }
        this.permissions.add(permission);
        permission.getRoles().add(this);
        return this;

    }

    public Role removePermission(Permission permission){
        if(permission == null){
            throw new IllegalArgumentException();
        }
        this.permissions.remove(permission);
        permission.getRoles().remove(this);
        return this;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

}
