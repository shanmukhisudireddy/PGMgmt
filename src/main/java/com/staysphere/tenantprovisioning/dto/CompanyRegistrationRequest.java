package com.staysphere.tenantprovisioning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CompanyRegistrationRequest {

    @NotBlank(message = "Company Name is required") @Size (min = 6, max = 40)
    private String companyName;


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
