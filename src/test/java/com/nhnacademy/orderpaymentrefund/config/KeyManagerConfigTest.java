package com.nhnacademy.orderpaymentrefund.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class KeyManagerConfigTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private KeyManagerConfig keyManagerConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(keyManagerConfig, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(keyManagerConfig, "accessKeyId", "test-access-key-id");
        ReflectionTestUtils.setField(keyManagerConfig, "accessKeySecret", "test-access-key-secret");
        ReflectionTestUtils.setField(keyManagerConfig, "rabbitmqUsernameKey", "rabbitmq-username-key");
        ReflectionTestUtils.setField(keyManagerConfig, "rabbitmqHostKey", "rabbitmq-host-key");
        ReflectionTestUtils.setField(keyManagerConfig, "rabbitmqPasswordKey", "rabbitmq-password-key");
        ReflectionTestUtils.setField(keyManagerConfig, "rabbitmqPortKey", "rabbitmq-port-key");
        ReflectionTestUtils.setField(keyManagerConfig, "mysqlUrlKey", "mysql-url-key");
        ReflectionTestUtils.setField(keyManagerConfig, "mysqlUsernameKey", "mysql-username-key");
        ReflectionTestUtils.setField(keyManagerConfig, "mysqlPasswordKey", "mysql-password-key");

    }

    private void mockRestTemplateResponse(String secretValue) {
        String jsonResponse = "{\"body\":{\"secret\":\"" + secretValue + "\"}}";
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);
    }

    @Test
    void testRabbitHost() {
        mockRestTemplateResponse("localhost");
        assertEquals("localhost", keyManagerConfig.rabbitHost());
    }

    @Test
    void testRabbitPassword() {
        mockRestTemplateResponse("password");
        assertEquals("password", keyManagerConfig.rabbitPassword());
    }

    @Test
    void testRabbitUsername() {
        mockRestTemplateResponse("username");
        assertEquals("username", keyManagerConfig.rabbitUsername());
    }

    @Test
    void testRabbitPort() {
        mockRestTemplateResponse("5672");
        assertEquals(5672, keyManagerConfig.rabbitPort());
    }

    @Test
    void testMysqlUrl() {
        mockRestTemplateResponse("jdbc:mysql://localhost:3306/db");
        assertEquals("jdbc:mysql://localhost:3306/db", keyManagerConfig.mysqlUrl());
    }

    @Test
    void testMysqlPassword() {
        mockRestTemplateResponse("mysql-password");
        assertEquals("mysql-password", keyManagerConfig.mysqlPassword());
    }

    @Test
    void testMysqlUsername() {
        mockRestTemplateResponse("mysql-username");
        assertEquals("mysql-username", keyManagerConfig.mysqlUsername());
    }




}