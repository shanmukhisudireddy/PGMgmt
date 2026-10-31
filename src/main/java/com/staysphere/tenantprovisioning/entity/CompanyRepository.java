package com.staysphere.tenantprovisioning.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staysphere.tenantprovisioning.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
    
}
