package com.staysphere.tenant.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.staysphere.companymgmt.entity.Company;
import com.staysphere.companymgmt.enums.CompanyStatusEnum;
import com.staysphere.companymgmt.repository.CompanyRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component 
public class TenantDataSourceManager {

    private final Map<UUID, DataSource> tenantDataSources = new ConcurrentHashMap<>();

    public final CompanyRepository companyRepository;

    public TenantDataSourceManager(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String centralDatabaseUrl;

    public DataSource getDataSource(UUID tenantId){
        return tenantDataSources.computeIfAbsent(tenantId, this::createDataSource);
    }

    private DataSource createDataSource(UUID tenantId){
        Company company = companyRepository.findByTenantId(tenantId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Tenant not found"));

        if (company.getStatus() != CompanyStatusEnum.ACTIVE) {
            throw new IllegalStateException("Company is not active");
        }

        String tenantUrl = centralDatabaseUrl.substring(
                0, centralDatabaseUrl.lastIndexOf("/") + 1
        ) + company.getDatabaseName();

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(tenantUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        config.setPoolName("Tenant-" + tenantId);

        return new HikariDataSource(config);

    }
    
    public void removeDataSource(UUID tenantId) {
        DataSource dataSource = tenantDataSources.remove(tenantId);
        if(dataSource instanceof HikariDataSource hikariDataSource){
            hikariDataSource.close();
        }

    }

}
