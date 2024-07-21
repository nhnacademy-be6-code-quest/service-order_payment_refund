package com.nhnacademy.orderpaymentrefund.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

class RabbitConfigTest {

    private RabbitConfig rabbitConfig;

    @BeforeEach
    void setUp() {
        rabbitConfig = new RabbitConfig("localhost", 5672, "guest", "guest");
    }

    @Test
    void testConnectionFactory() {
        ConnectionFactory connectionFactory = rabbitConfig.connectionFactory();

        assertNotNull(connectionFactory);
        assertTrue(connectionFactory instanceof CachingConnectionFactory);

        CachingConnectionFactory cachingConnectionFactory = (CachingConnectionFactory) connectionFactory;
        assertEquals("localhost", cachingConnectionFactory.getHost());
        assertEquals(5672, cachingConnectionFactory.getPort());
        assertEquals("guest", cachingConnectionFactory.getUsername());
        // 비밀번호는 보안상의 이유로 직접 확인하기 어려우므로 테스트하지 않습니다.
    }

    @Test
    void testRabbitTemplate() {
        ConnectionFactory connectionFactory = rabbitConfig.connectionFactory();
        RabbitTemplate rabbitTemplate = rabbitConfig.rabbitTemplate(connectionFactory);

        assertNotNull(rabbitTemplate);
        assertEquals(connectionFactory, rabbitTemplate.getConnectionFactory());
        assertTrue(rabbitTemplate.getMessageConverter() instanceof Jackson2JsonMessageConverter);
    }
}