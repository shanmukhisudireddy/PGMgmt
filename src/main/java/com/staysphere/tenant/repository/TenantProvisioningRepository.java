package com.staysphere.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staysphere.tenant.entity.TenantProvisioning;

import java.util.Optional;


public interface TenantProvisioningRepository extends JpaRepository<TenantProvisioning, Long>{
    Optional<TenantProvisioning>
findTopByCompany_IdOrderByAttemptNumberDesc(Long companyId);

    Optional<TenantProvisioning> findByProvisioningId(Long provisioningId);
}
