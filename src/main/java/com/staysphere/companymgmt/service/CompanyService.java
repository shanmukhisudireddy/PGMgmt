package com.staysphere.companymgmt.service;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Optional;
import java.util.List;

import org.springframework.stereotype.Service;

import com.staysphere.companymgmt.dto.CompanyRegistrationRequest;
import com.staysphere.companymgmt.entity.Company;
import com.staysphere.companymgmt.enums.CompanyStatusEnum;
import com.staysphere.companymgmt.exception.CompanyNotFoundException;
import com.staysphere.companymgmt.exception.InvalidStatusUpdateException;
import com.staysphere.companymgmt.repository.CompanyRepository;
import com.staysphere.tenant.TenantProvisioningService;
import com.staysphere.tenant.config.TenantDataSourceManager;
import com.staysphere.tenant.datasource.TenantDatabaseProvisioningService;
import com.staysphere.tenant.entity.TenantProvisioning;
import com.staysphere.tenant.enums.ProvisioningStepEnum;
import com.staysphere.tenant.enums.TenantProvisioningStatusEnum;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final TenantDataSourceManager tenantDataSourceManager;

    private final TenantDatabaseProvisioningService tenantDatabaseProvisioningService;

    private final TenantProvisioningService tenantProvisioningService;

    private static final SecureRandom RANDOM = new SecureRandom();

    public CompanyService(CompanyRepository companyRepository, TenantDataSourceManager tenantDataSourceManager, 
        TenantDatabaseProvisioningService tenantDatabaseProvisioningService, TenantProvisioningService tenantProvisioningService) {
        this.companyRepository = companyRepository;
        this.tenantDataSourceManager = tenantDataSourceManager;
        this.tenantDatabaseProvisioningService = tenantDatabaseProvisioningService;
        this.tenantProvisioningService = tenantProvisioningService;
    }

    public Company registerCompany(CompanyRegistrationRequest request) {

        String companyCode = generateCompanyCode(
                request.getCompanyName()
        );
        String databaseName = "tenant_"+companyCode.toLowerCase(Locale.ROOT);
        Company company = new Company();

        company.setCompanyName(request.getCompanyName());
        company.setCompanyCode(companyCode);
        company.setDatabaseName(databaseName);
    
        return companyRepository.save(company);
    }

    private String generateCompanyCode(String companyName) {

        // Extract first four letters
        String prefix = companyName
                .replaceAll("[^a-zA-Z]", "")
                .toUpperCase(Locale.ROOT);

        if (prefix.length() < 4) {
            throw new IllegalArgumentException(
                    "Company name must contain at least 4 letters"
            );
        }

        prefix = prefix.substring(0, 4);

        // Try generating a unique company code
        for (int i = 0; i < 20; i++) {

            int randomNumber = 1000 + RANDOM.nextInt(9000);

            String companyCode = prefix + randomNumber;

            if (!companyRepository.existsByCompanyCode(companyCode)) {
                return companyCode;
            }
        }

        throw new IllegalStateException(
                "Unable to generate a unique company code"
        );
    }

    public Company getCompanyById(Long id){
        Optional<Company> company = companyRepository.findById(id);
        return company.orElseThrow(()->new CompanyNotFoundException("Company with Id " + id + " Not Found"));
    }

    public Company getCompanyByCode(String companyCode){
        Optional<Company> company = companyRepository.findByCompanyCode(companyCode);
        return company.orElseThrow(()->new CompanyNotFoundException("Company with code " + companyCode + " Not Found"));
    }

    public List<Company> getCompanies(){
        return companyRepository.findAll();
    }

    public Company updateStatus(Long id, CompanyStatusEnum status){
        Company company = getCompanyById(id);
        if(company.getStatus()==CompanyStatusEnum.PENDING && status==CompanyStatusEnum.PROVISIONING){
            company.setStatus(status);
            companyRepository.save(company);
        }
        else if(company.getStatus()==CompanyStatusEnum.ACTIVE && status==CompanyStatusEnum.DEPROVISIONING){
            company.setStatus(status);
            tenantDataSourceManager.removeDataSource(company.getTenantId());
            companyRepository.save(company);
        }
        else{
            String msg = "Company status updation is not allowed from "+company.getStatus()+ " to "+ status;
            throw new InvalidStatusUpdateException(msg);
        }
        return company;
    }

    public Company approveStatus(Long id, CompanyStatusEnum status){
        Company company = getCompanyById(id);
        String expectedDatabaseName;
        if ((company.getStatus() == CompanyStatusEnum.PROVISIONING
        || company.getStatus() == CompanyStatusEnum.FAILED)
        && status == CompanyStatusEnum.ACTIVE) {

        expectedDatabaseName =
                "tenant_" + company.getCompanyCode().toLowerCase(Locale.ROOT);

        if (!expectedDatabaseName.equals(company.getDatabaseName())) {
            throw new IllegalStateException(
                    "Tenant database mapping mismatch. Provisioning aborted."
            );
        }

        TenantProvisioning tenantProvisioning =
                tenantProvisioningService.createProvisioningAttempt(company);

        company.setStatus(CompanyStatusEnum.PROVISIONING);
        companyRepository.save(company);

        try {

            // STEP 1: DATABASE

            tenantProvisioningService.updateStepStatus(
                    tenantProvisioning.getProvisioningId(),
                    ProvisioningStepEnum.DATABASE,
                    TenantProvisioningStatusEnum.IN_PROGRESS
            );

            tenantDatabaseProvisioningService.createTenantDatabase(
                    company.getDatabaseName()
            );

            tenantProvisioningService.updateStepStatus(
                    tenantProvisioning.getProvisioningId(),
                    ProvisioningStepEnum.DATABASE,
                    TenantProvisioningStatusEnum.COMPLETED
            );

            // STEP 2: SCHEMA - pending implementation

            // STEP 3: ROLE - pending implementation

            // STEP 4: PERMISSION - pending implementation

        } catch (Exception e) {

            tenantProvisioningService.updateStepStatus(
                    tenantProvisioning.getProvisioningId(),
                    ProvisioningStepEnum.DATABASE,
                    TenantProvisioningStatusEnum.FAILED
            );

            tenantProvisioningService.updateLastError(
                    tenantProvisioning.getProvisioningId(),
                    e.getMessage()
            );

            company.setStatus(CompanyStatusEnum.FAILED);
            companyRepository.save(company);

            throw new IllegalStateException(
                    "Tenant provisioning failed for company: "
                            + company.getCompanyCode(), e
            );
        }

                companyRepository.save(company);
            }
        else if((company.getStatus()==CompanyStatusEnum.ACTIVE && status==CompanyStatusEnum.SUSPENDED) ||
            company.getStatus()==CompanyStatusEnum.SUSPENDED && status==CompanyStatusEnum.ACTIVE){
            tenantDataSourceManager.removeDataSource(company.getTenantId());
            company.setStatus(status);
            companyRepository.save(company);
        }
        else if(company.getStatus()==CompanyStatusEnum.DEPROVISIONING
            && status==CompanyStatusEnum.DEPROVISIONED){
            expectedDatabaseName =
            "tenant_" + company.getCompanyCode().toLowerCase(Locale.ROOT);

            if (!expectedDatabaseName.equals(company.getDatabaseName())) {
                throw new IllegalStateException(
                        "Tenant database mapping mismatch. Deletion aborted."
                );
            }
            tenantDataSourceManager.removeDataSource(company.getTenantId());
            tenantDatabaseProvisioningService.dropTenantDatabase(company.getDatabaseName());
            company.setStatus(status);
            companyRepository.save(company);
        }
        else{
            String msg = "Company status updation is not allowed from "+company.getStatus()+ " to "+ status;
            throw new InvalidStatusUpdateException(msg);
        }
        return company;
    }

}