package com.nua.core.config.datasource;

import com.nua.core.exceptions.exceptions.TransactionException;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties
public class TokenDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(TokenDataSourceConfig.class);

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.token")
    public DataSourceProperties tokenDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "tokenDataSource")
    public DataSource tokenDataSource(@Qualifier("tokenDataSourceProperties") DataSourceProperties tokenDataSourceProperties) {
        log.info("Iniciando H2 Token DataSource");
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:tokenDb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false")
                .username("sa")
                .password("")
                .build();
    }

    @Bean(name = "tokenEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tokenEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("tokenDataSource") DataSource tokenDataSource) {
        try {
            log.info("Creando EntityManagerFactory para Token");
            return builder
                    .dataSource(tokenDataSource)
                    .packages("com.nua.core.security")
                    .persistenceUnit("Token")
                    .properties(Map.of(
                            "hibernate.hbm2ddl.auto", "update",
                            "hibernate.dialect", "org.hibernate.dialect.H2Dialect"
                    ))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new TransactionException(e.getMessage(), "x");
        }
    }

    @Bean(name = "tokenTransactionManager")
    public PlatformTransactionManager tokenTransactionManager(
            @Qualifier("tokenEntityManagerFactory") EntityManagerFactory tokenEntityManagerFactory) {
        return new JpaTransactionManager(tokenEntityManagerFactory);
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }



}