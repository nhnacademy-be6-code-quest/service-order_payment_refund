package com.nhnacademy.orderpaymentrefund.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

class SwaggerTest {
    @Test
    void testCustomOpenAPI_shouldCreateDefaultOpenAPIObject() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getComponents()).isNotNull();
        assertThat(openAPI.getInfo()).isNotNull();

        assertThat(openAPI.getInfo().getTitle()).isEqualTo("order-payment-refund-api");
        assertThat(openAPI.getInfo().getVersion()).isEqualTo("1.0");
        assertThat(openAPI.getInfo().getDescription()).isEqualTo("order-payment-refund-api");
    }
}


