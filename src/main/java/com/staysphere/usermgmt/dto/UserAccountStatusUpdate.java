package com.staysphere.usermgmt.dto;

import com.staysphere.usermgmt.enums.AccountStatusEnum;

import jakarta.validation.constraints.NotNull;

public class UserAccountStatusUpdate {
    @NotNull (message = "Account status is required")
    private AccountStatusEnum accountStatus;
    
    public UserAccountStatusUpdate(AccountStatusEnum accountStatus){
        this.accountStatus = accountStatus;
    }
    
    public AccountStatusEnum getAccountStatus(){
        return this.accountStatus;
    }

    public void setAccountStatus(AccountStatusEnum accountStatus){
        this.accountStatus = accountStatus;
    }
}
