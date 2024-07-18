package com.nhnacademy.orderpaymentrefund.config;

import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {
    private final String mysqlUrl;
    private final String mysqlUsername;
    private final String mysqlPassword;

    @Value("${datasource.driver-class-name}")
    private String driverClassName;
    @Value("${datasource.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${datasource.validation-query}")
    private String validationQuery;
    @Value("${datasource.initial-size}")
    private int initialSize;
    @Value("${datasource.max-idle}")
    private int maxIdle;
    @Value("${datasource.max-total}")
    private int maxTotal;
    @Value("${datasource.min-idle}")
    private int minIdle;
    @Value("${datasource.max-open-prepared-statements}")
    private int maxOpenPreparedStatements;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(mysqlUrl);
        dataSource.setUsername(mysqlUsername);
        dataSource.setPassword(mysqlPassword);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxTotal(maxTotal);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);

        return dataSource;
    }

}