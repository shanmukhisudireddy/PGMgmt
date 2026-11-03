package com.staysphere.tenantprovisioning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staysphere.tenantprovisioning.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{

    boolean existsByCompanyCode(String companyCode);
    
}
