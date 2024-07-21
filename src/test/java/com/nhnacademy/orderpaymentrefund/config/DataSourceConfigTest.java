package com.nhnacademy.orderpaymentrefund.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class DataSourceConfigTest {

    private DataSourceConfig dataSourceConfig;

    @BeforeEach
    void setUp() {
        dataSourceConfig = new DataSourceConfig(
                "jdbc:mysql://localhost:3306/testdb",
                "testuser",
                "testpassword"
        );

        ReflectionTestUtils.setField(dataSourceConfig, "driverClassName", "com.mysql.cj.jdbc.Driver");
        ReflectionTestUtils.setField(dataSourceConfig, "testOnBorrow", true);
        ReflectionTestUtils.setField(dataSourceConfig, "validationQuery", "SELECT 1");
        ReflectionTestUtils.setField(dataSourceConfig, "initialSize", 2);
        ReflectionTestUtils.setField(dataSourceConfig, "maxIdle", 10);
        ReflectionTestUtils.setField(dataSourceConfig, "maxTotal", 10);
        ReflectionTestUtils.setField(dataSourceConfig, "minIdle", 2);
        ReflectionTestUtils.setField(dataSourceConfig, "maxOpenPreparedStatements", 3000);
    }

    @Test
    void testDataSource() {
        DataSource dataSource = dataSourceConfig.dataSource();

        assertNotNull(dataSource);
        assertTrue(dataSource instanceof BasicDataSource);

        BasicDataSource basicDataSource = (BasicDataSource) dataSource;

        assertEquals("jdbc:mysql://localhost:3306/testdb", basicDataSource.getUrl());
        assertEquals("com.mysql.cj.jdbc.Driver", basicDataSource.getDriverClassName());
        assertTrue(basicDataSource.getTestOnBorrow());
        assertEquals("SELECT 1", basicDataSource.getValidationQuery());
        assertEquals(2, basicDataSource.getInitialSize());
        assertEquals(10, basicDataSource.getMaxIdle());
        assertEquals(10, basicDataSource.getMaxTotal());
        assertEquals(2, basicDataSource.getMinIdle());
        assertEquals(3000, basicDataSource.getMaxOpenPreparedStatements());
    }
}