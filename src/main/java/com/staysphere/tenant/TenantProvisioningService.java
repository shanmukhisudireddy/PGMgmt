package com.staysphere.tenant;

import com.staysphere.companymgmt.entity.Company;
import com.staysphere.tenant.entity.TenantProvisioning;
import com.staysphere.tenant.enums.ProvisioningStepEnum;
import com.staysphere.tenant.enums.TenantProvisioningStatusEnum;
import com.staysphere.tenant.repository.TenantProvisioningRepository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service 
public class TenantProvisioningService {


    private final TenantProvisioningRepository tenantProvisioningRepository;

    public TenantProvisioningService(TenantProvisioningRepository tenantProvisioningRepository){
        this.tenantProvisioningRepository = tenantProvisioningRepository;
    }

    

    public TenantProvisioning createProvisioningAttempt(Company company){
    TenantProvisioning tenantProvisioning = new TenantProvisioning();
    Optional<TenantProvisioning> previousAttempt =
    tenantProvisioningRepository
        .findTopByCompany_IdOrderByAttemptNumberDesc(company.getId());

    int nextAttemptNumber = previousAttempt
    .map(attempt -> attempt.getAttemptNumber() + 1)
    .orElse(1);
    tenantProvisioning.setCompany(company);
    tenantProvisioning.setAttemptNumber(nextAttemptNumber);
    tenantProvisioning.setDatabaseStatus(TenantProvisioningStatusEnum.PENDING);
    tenantProvisioning.setPermissionStatus(TenantProvisioningStatusEnum.PENDING);
    tenantProvisioning.setRoleStatus(TenantProvisioningStatusEnum.PENDING);
    tenantProvisioning.setSchemaStatus(TenantProvisioningStatusEnum.PENDING);
    tenantProvisioning.setCreatedAt(Instant.now());
    tenantProvisioning.setUpdatedAt(Instant.now());
    tenantProvisioning.setOverallStatus(TenantProvisioningStatusEnum.PENDING);
    return tenantProvisioningRepository.save(tenantProvisioning);
    }

    public TenantProvisioning updateStepStatus(
        Long provisioningId,
        ProvisioningStepEnum step,
        TenantProvisioningStatusEnum status
){
    TenantProvisioning tenantProvisioning = tenantProvisioningRepository.findById(provisioningId)
    .orElseThrow(()-> new IllegalArgumentException("Provisioning attempt not found: " + provisioningId)) ;
    switch(step){
        case DATABASE: 
        tenantProvisioning.setDatabaseStatus(status);
        break;
        case SCHEMA: 
        tenantProvisioning.setSchemaStatus(status);
        break;
        case ROLE: 
        tenantProvisioning.setRoleStatus(status);
        break;
        case PERMISSION: 
        tenantProvisioning.setPermissionStatus(status);
        break;
        default:
            throw new IllegalArgumentException(
                    "Unsupported provisioning step: " + step);
    }
     tenantProvisioning.setOverallStatus(
            calculateOverallStatus(tenantProvisioning)
    );
    tenantProvisioning.setUpdatedAt(Instant.now());
    return tenantProvisioningRepository.save(tenantProvisioning);
}

    private TenantProvisioningStatusEnum calculateOverallStatus(TenantProvisioning provisioning) {

        TenantProvisioningStatusEnum databaseStatus =
                provisioning.getDatabaseStatus();

        TenantProvisioningStatusEnum schemaStatus =
                provisioning.getSchemaStatus();

        TenantProvisioningStatusEnum roleStatus =
                provisioning.getRoleStatus();

        TenantProvisioningStatusEnum permissionStatus =
                provisioning.getPermissionStatus();

        // If any step has failed, the overall provisioning has failed.
        if (databaseStatus == TenantProvisioningStatusEnum.FAILED
                || schemaStatus == TenantProvisioningStatusEnum.FAILED
                || roleStatus == TenantProvisioningStatusEnum.FAILED
                || permissionStatus == TenantProvisioningStatusEnum.FAILED) {

            return TenantProvisioningStatusEnum.FAILED;
        }

        // Provisioning is complete only when every step is completed.
        if (databaseStatus == TenantProvisioningStatusEnum.COMPLETED
                && schemaStatus == TenantProvisioningStatusEnum.COMPLETED
                && roleStatus == TenantProvisioningStatusEnum.COMPLETED
                && permissionStatus == TenantProvisioningStatusEnum.COMPLETED) {

            return TenantProvisioningStatusEnum.COMPLETED;
        }

        // At least one step has started, but provisioning is not complete.
        if (databaseStatus == TenantProvisioningStatusEnum.IN_PROGRESS
                || schemaStatus == TenantProvisioningStatusEnum.IN_PROGRESS
                || roleStatus == TenantProvisioningStatusEnum.IN_PROGRESS
                || permissionStatus == TenantProvisioningStatusEnum.IN_PROGRESS
                || databaseStatus == TenantProvisioningStatusEnum.COMPLETED
                || schemaStatus == TenantProvisioningStatusEnum.COMPLETED
                || roleStatus == TenantProvisioningStatusEnum.COMPLETED
                || permissionStatus == TenantProvisioningStatusEnum.COMPLETED) {

            return TenantProvisioningStatusEnum.IN_PROGRESS;
        }

        return TenantProvisioningStatusEnum.PENDING;
    }

    public TenantProvisioning updateLastError(Long provisioningId,String errorMessage){
            TenantProvisioning tenantProvisioning = tenantProvisioningRepository.findById(provisioningId)
            .orElseThrow(()-> new IllegalArgumentException("ProvisioningId not found"));
            tenantProvisioning.setLastError(errorMessage);
            tenantProvisioning.setUpdatedAt(Instant.now());
            
            return tenantProvisioningRepository.save(tenantProvisioning);
        }

    public TenantProvisioning clearLastError(Long provisioningId) {

    TenantProvisioning tenantProvisioning =
            tenantProvisioningRepository.findById(provisioningId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Provisioning attempt not found: " + provisioningId));

    tenantProvisioning.setLastError(null);
    tenantProvisioning.setUpdatedAt(Instant.now());

    return tenantProvisioningRepository.save(tenantProvisioning);
}
}
