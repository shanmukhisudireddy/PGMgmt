package com.staysphere.companymgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staysphere.companymgmt.entity.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, Long>{

    boolean existsByCompanyCode(String companyCode);

    Optional<Company> findByCompanyCode(String companyCode);

    Optional<Company> findByTenantId(UUID tenantId);
    
}
