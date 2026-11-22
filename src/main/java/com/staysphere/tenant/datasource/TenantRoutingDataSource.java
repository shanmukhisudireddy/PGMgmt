package com.staysphere.tenant.datasource;

import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import com.staysphere.tenant.context.TenantContext;
import com.staysphere.tenant.config.TenantDataSourceManager;

@Component 
public class TenantRoutingDataSource extends AbstractRoutingDataSource{

    private final TenantDataSourceManager tenantDataSourceManager;

    public TenantRoutingDataSource(TenantDataSourceManager tenantDataSourceManager){
        this.tenantDataSourceManager = tenantDataSourceManager;
    }

    @Override 
    protected UUID determineCurrentLookupKey(){
        return TenantContext.getTenantId();
    }

    @Override 
    protected DataSource determineTargetDataSource(){
        UUID tenantId = determineCurrentLookupKey();
        if(tenantId == null){
            throw new IllegalArgumentException("Tenant not found");
        }

        return tenantDataSourceManager.getDataSource(tenantId);
    }

    @Override 
    public void afterPropertiesSet() {
	}
}
