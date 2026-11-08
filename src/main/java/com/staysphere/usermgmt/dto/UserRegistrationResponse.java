package com.staysphere.usermgmt.dto;

import java.time.Instant;

import com.staysphere.usermgmt.enums.AccountStatusEnum;

public class UserRegistrationResponse {
    private final Long userId;

    private final String fullName;

    private final String emailId;

    private final AccountStatusEnum accountStatus;

    private final Instant createdAt;

    public UserRegistrationResponse(Long userId, String fullName, String emailId, AccountStatusEnum accountStatus, Instant createdAt){
        this.userId = userId;
        this.fullName = fullName;
        this.emailId = emailId;
        this.accountStatus = accountStatus;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public AccountStatusEnum getAccountStatus() {
        return accountStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    

}
