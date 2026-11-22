package com.staysphere.tenant.context;

import java.util.UUID;

import com.staysphere.companymgmt.repository.CompanyRepository;

public class TenantContext {

    private static final ThreadLocal<UUID> CURRENT_TENANT = new ThreadLocal<>();

    private final CompanyRepository companyRepository;

    public TenantContext(CompanyRepository companyRepository){
        this.companyRepository = companyRepository;
    }

        public static UUID getTenantId(){
            return CURRENT_TENANT.get();
        } 

        public static void setTenantId(UUID tenantId){
            CURRENT_TENANT.set(tenantId);
        }

        public static void clear(){
            CURRENT_TENANT.remove(); 
    }
}
