package com.staysphere.tenant.datasource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
@Service
public class TenantDatabaseProvisioningService {

    private final JdbcTemplate jdbcTemplate;

    public TenantDatabaseProvisioningService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String createTenantDatabase(String databaseName) {

        if (databaseName == null ||
                !Pattern.matches("tenant_[A-Za-z0-9]+", databaseName)) {
            throw new IllegalArgumentException("Invalid database name");
        }
        String sql = "CREATE DATABASE IF NOT EXISTS `" + databaseName + "`";

        jdbcTemplate.execute(sql);

        return databaseName;
    }

    public void dropTenantDatabase(String databaseName){
        if (databaseName == null ||
                !Pattern.matches("tenant_[A-Za-z0-9]+", databaseName)) {
            throw new IllegalArgumentException("Invalid database name");
        }
        String sql = "DROP DATABASE `" + databaseName + "`";

        jdbcTemplate.execute(sql);
    }

    
}