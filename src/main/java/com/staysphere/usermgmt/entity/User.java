package com.staysphere.usermgmt.entity;


import java.time.Instant;

import com.staysphere.usermgmt.enums.AccountStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table (name = "users", uniqueConstraints = 
    {@UniqueConstraint(name = "uk_emailid",
        columnNames = "emailid"
)
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
    protected String passwordHash;

    @Column (nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatusEnum accountStatus;
    
    @Column (nullable = false, updatable = false)
    private Instant createdAt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        userId = userId;
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

    public void setAccountStatus(AccountStatusEnum accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();

        if (accountStatus == null) {
            accountStatus = AccountStatusEnum.PENDING;
        }


    }


}
