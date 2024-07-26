package com.nhnacademy.orderpaymentrefund.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

class RestTemplateConfigTest {
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(TestConfig.class);
    }

    @Test
    public void restTemplateBeanShouldBeConfigured() {
        // Given - Context loaded

        // When
        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        // Then
        assertThat(restTemplate).isNotNull();
    }

    @Configuration
    static class TestConfig extends RestTemplateConfig {
        @Bean
        @Override
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
