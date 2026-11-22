package com.staysphere.companymgmt.dto;

import com.staysphere.companymgmt.enums.CompanyStatusEnum;

import jakarta.validation.constraints.NotNull;

public class CompanyStatusUpdateRequest {
    @NotNull (message = "Company Status is required")
    private CompanyStatusEnum status;

    public CompanyStatusUpdateRequest(CompanyStatusEnum status){
        this.status = status;
    }

    public CompanyStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CompanyStatusEnum status) {
        this.status = status;
    }
    
    
}
