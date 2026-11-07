package com.staysphere.tenantprovisioning.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import com.staysphere.tenantprovisioning.service.CompanyService;
import com.staysphere.tenantprovisioning.dto.CompanyRegistrationRequest;
import com.staysphere.tenantprovisioning.dto.CompanyRegistrationResponse;
import com.staysphere.tenantprovisioning.dto.CompanyStatusUpdateRequest;
import com.staysphere.tenantprovisioning.entity.Company;
import com.staysphere.tenantprovisioning.enums.CompanyStatusEnum;
import com.staysphere.tenantprovisioning.exception.CompanyNotFoundException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/{id}")
    public ResponseEntity<CompanyRegistrationResponse> getCompany(@PathVariable("id") Long id) {
        Company company = companyService.getCompanyById(id);
        CompanyRegistrationResponse response = new CompanyRegistrationResponse(company.getCompanyName(), company.getTenantId(), company.getCompanyCode(), company.getStatus());
    
        return ResponseEntity.ok(response);
    }

    @GetMapping("GetAllCompanies")
    public ResponseEntity<List<CompanyRegistrationResponse>> getAllCompanies() {
        List<Company> companies = companyService.getCompanies();
        List<CompanyRegistrationResponse> response = new ArrayList<>();
        for (Company company : companies) {
            response.add(new CompanyRegistrationResponse(company.getCompanyName(), company.getTenantId(), company.getCompanyCode(), company.getStatus()));
        }
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping ("/{id}/status")
    public ResponseEntity<CompanyRegistrationResponse> setCompanyStatus(@PathVariable ("id") Long id, @RequestBody @Valid CompanyStatusUpdateRequest request){
        Company company = companyService.updateStatus(id, request.getStatus());
        CompanyRegistrationResponse response = new CompanyRegistrationResponse(company.getCompanyName(), company.getTenantId(), company.getCompanyCode(), company.getStatus());
        return ResponseEntity.ok(response);
    }
    
    
}
