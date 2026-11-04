package com.staysphere.tenantprovisioning.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staysphere.tenantprovisioning.service.CompanyService;
import com.staysphere.tenantprovisioning.dto.CompanyRegistrationRequest;
import com.staysphere.tenantprovisioning.dto.CompanyRegistrationResponse;
import com.staysphere.tenantprovisioning.entity.Company;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping ("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService= companyService;
    }

    @PostMapping("/register")
    public ResponseEntity<CompanyRegistrationResponse> registerCompany(@RequestBody @Valid CompanyRegistrationRequest req) {

        Company company = companyService.registerCompany(req);

        CompanyRegistrationResponse response = 
                                        new CompanyRegistrationResponse(
                                                    company.getCompanyName(),
                                                    company.getTenantId(),
                                                    company.getCompanyCode(),
                                                    company.getStatus()
                                            );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    
}
