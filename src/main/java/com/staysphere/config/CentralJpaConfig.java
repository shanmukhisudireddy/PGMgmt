
package com.staysphere.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    basePackages = {
        "com.staysphere.companymgmt.repository",
        "com.staysphere.tenant.repository"
    },
    entityManagerFactoryRef = "centralEntityManagerFactory",
    transactionManagerRef = "centralTransactionManager"
)
public class CentralJpaConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties centralDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "centralDataSource")
    @Primary
    public DataSource centralDataSource(
            @Qualifier("centralDataSourceProperties")
            DataSourceProperties properties) {

        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "centralEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean centralEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("centralDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages(
                    "com.staysphere.companymgmt.entity",
                    "com.staysphere.tenant.entity"
                )
                .persistenceUnit("central")
                .build();
    }

    @Bean(name = "centralTransactionManager")
    @Primary
    public PlatformTransactionManager centralTransactionManager(
            @Qualifier("centralEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean factory) {

        return new JpaTransactionManager(factory.getObject());
    }
}
