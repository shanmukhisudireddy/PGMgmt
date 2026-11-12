package com.staysphere.usermgmt.entity;


import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.staysphere.usermgmt.enums.AccountStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.ManyToMany;

@Entity 
@Table (name = "users", uniqueConstraints = 
    {@UniqueConstraint(name = "uk_emailid",
        columnNames = "emailid"
    ),
    @UniqueConstraint(name ="uk_username",
    columnNames = "username")
    } 
    )

public class User {
    @Id 
    @GeneratedValue (strategy =
            GenerationType.IDENTITY)
    private Long userId;

    @Column (nullable = false)
    private String fullName;
    
    @Column (name = "emailid", nullable = false)
    @Email 
    @NotBlank 
    private String emailId;

    @Column (nullable = false)
    private String passwordHash;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatusEnum accountStatus;
    
    @Column (nullable = false, updatable = false)
    private Instant createdAt;

    @Column (name = "username", nullable=false, updatable = false, length = 8)
    private String userName;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn (name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public Long getUserId() {
        return userId;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public AccountStatusEnum getAccountStatus() {
        return accountStatus;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setAccountStatus(AccountStatusEnum accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User assignRoles(Role role){
        if(role == null){
            throw new IllegalArgumentException("Role can not be null");
        }
        this.roles.add(role);
        role.getUsers().add(this);
        return this;
    }

    public User deleteRoles(Role role){
        if(role == null){
            throw new IllegalArgumentException("Role can not be null");
        }
        this.roles.remove(role);
        role.getUsers().remove(this);
        return this;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();

        if (accountStatus == null) {
            accountStatus = AccountStatusEnum.PENDING;
        }


    }


}
