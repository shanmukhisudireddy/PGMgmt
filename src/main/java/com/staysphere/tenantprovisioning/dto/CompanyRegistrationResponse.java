package com.staysphere.tenantprovisioning.dto;

import java.util.UUID;

import com.staysphere.tenantprovisioning.enums.CompanyStatusEnum;

public class CompanyRegistrationResponse {

    private final String companyName;

    private final UUID tenantId;

    private final String companyCode;

    private final CompanyStatusEnum status;

    public CompanyRegistrationResponse(String companyName, UUID tenantId, String companyCode, CompanyStatusEnum status){
        this.companyName = companyName;
        this.tenantId = tenantId;
        this.companyCode = companyCode;
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public CompanyStatusEnum getStatus() {
        return status;
    }


}
