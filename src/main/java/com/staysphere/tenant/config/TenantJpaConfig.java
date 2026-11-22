package com.staysphere.tenant.config;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.staysphere.tenant.datasource.TenantRoutingDataSource;

@Configuration 
@EnableJpaRepositories (
    basePackages = {"com.staysphere.usermgmt.repository"},
    entityManagerFactoryRef = "tenantEntityManagerFactory",
    transactionManagerRef = "tenantTransactionManager"
)
public class TenantJpaConfig {

    @Bean(name = "tenantDataSource")
    public DataSource tenantDataSource(
            TenantRoutingDataSource routingDataSource) {
        
        LazyConnectionDataSourceProxy proxy =
            new LazyConnectionDataSourceProxy(routingDataSource);

        proxy.setDefaultAutoCommit(true);
        proxy.setDefaultTransactionIsolation(
                Connection.TRANSACTION_REPEATABLE_READ
        );

        return proxy;
    }

    @Bean(name = "tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("tenantDataSource") DataSource dataSource) {

        
        return builder
                .dataSource(dataSource)
                .packages("com.staysphere.usermgmt.entity")
                .persistenceUnit("tenant")
                .properties(Map.of(
                    "hibernate.boot.allow_jdbc_metadata_access", false,
                    "hibernate.dialect", "org.hibernate.dialect.MySQLDialect",
                    "hibernate.hbm2ddl.auto", "none"
                ))
                .build();

    }

    @Bean(name = "tenantTransactionManager")
    public PlatformTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean factory) {

        return new JpaTransactionManager(factory.getObject());
    }
}
