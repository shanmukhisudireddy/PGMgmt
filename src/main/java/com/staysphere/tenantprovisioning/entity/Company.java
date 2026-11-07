package com.staysphere.tenantprovisioning.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.staysphere.tenantprovisioning.enums.CompanyStatusEnum;

import jakarta.persistence.*;

@Entity
@Table(name = "companies",
    uniqueConstraints = {
        @UniqueConstraint (
            name = "uk_company_code",
            columnNames = "company_code"
        ),
        @UniqueConstraint(
            name = "uk_tenant_id",
            columnNames = "tenant_id"
        ),
        @UniqueConstraint(
            name = "uk_database_name",
            columnNames = "database_name"
        )
    }
)
public class Company {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)

    private UUID tenantId;

    private String companyName;
    
    @Column
    private String companyCode;

    @Column(name = "database_name")
    private String databaseName;

    @Enumerated (EnumType.STRING)
    private CompanyStatusEnum status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {

        if (tenantId == null){
            tenantId = UUID.randomUUID();
        }
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = CompanyStatusEnum.PENDING;
        }


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public CompanyStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CompanyStatusEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}