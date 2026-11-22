package com.staysphere.companymgmt.dto;

import jakarta.validation.constraints.NotBlank;

public class CompanyProfileUpdateRequest {
    @NotBlank (message = "CompanyName is expected")
    private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    
}
