package com.staysphere.tenant.entity;

import com.staysphere.companymgmt.entity.Company;
import com.staysphere.tenant.enums.TenantProvisioningStatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity 
@Table (name = "tenant_provisioning",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_company_attempt",
            columnNames = {"company_id", "attempt_number"}
        )
    }
)

public class TenantProvisioning {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "provisioning_id")
    private Long provisioningId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column (nullable = false, name = "attemptNumber")
    private int attemptNumber;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private TenantProvisioningStatusEnum databaseStatus;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private TenantProvisioningStatusEnum schemaStatus;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private TenantProvisioningStatusEnum roleStatus;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private TenantProvisioningStatusEnum permissionStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantProvisioningStatusEnum overallStatus;

    private String lastError;

    @Column (nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    public Long getProvisioningId() {
        return provisioningId;
    }

    public void setProvisioningId(Long provisioningId) {
        this.provisioningId = provisioningId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public TenantProvisioningStatusEnum getDatabaseStatus() {
        return databaseStatus;
    }

    public void setDatabaseStatus(TenantProvisioningStatusEnum databaseStatus) {
        this.databaseStatus = databaseStatus;
    }

    public TenantProvisioningStatusEnum getSchemaStatus() {
        return schemaStatus;
    }

    public void setSchemaStatus(TenantProvisioningStatusEnum schemaStatus) {
        this.schemaStatus = schemaStatus;
    }

    public TenantProvisioningStatusEnum getRoleStatus() {
        return roleStatus;
    }

    public void setRoleStatus(TenantProvisioningStatusEnum roleStatus) {
        this.roleStatus = roleStatus;
    }

    public TenantProvisioningStatusEnum getPermissionStatus() {
        return permissionStatus;
    }

    public void setPermissionStatus(TenantProvisioningStatusEnum permissionStatus) {
        this.permissionStatus = permissionStatus;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public TenantProvisioningStatusEnum getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(TenantProvisioningStatusEnum overallStatus) {
        this.overallStatus = overallStatus;
    }
}
