package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PostProcessRequiredPaymentResponseDtoTest {

    @Test
    void testNoArgsConstructor() {
        PostProcessRequiredPaymentResponseDto dto = new PostProcessRequiredPaymentResponseDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getOrderId()).isNull();
        assertThat(dto.getClientId()).isNull();
        assertThat(dto.getAmount()).isEqualTo(0);
        assertThat(dto.getPaymentMethodName()).isNull();
        assertThat(dto.getProductIdList()).isNull();
    }

    @Test
    void testBuilder() {
        Long orderId = 100L;
        Long clientId = 200L;
        long amount = 5000L;
        String paymentMethodName = "Credit Card";

        PostProcessRequiredPaymentResponseDto dto = PostProcessRequiredPaymentResponseDto.builder()
            .orderId(orderId)
            .clientId(clientId)
            .amount(amount)
            .paymentMethodName(paymentMethodName)
            .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getOrderId()).isEqualTo(orderId);
        assertThat(dto.getClientId()).isEqualTo(clientId);
        assertThat(dto.getAmount()).isEqualTo(amount);
        assertThat(dto.getPaymentMethodName()).isEqualTo(paymentMethodName);
        assertThat(dto.getProductIdList()).isNotNull();
        assertThat(dto.getProductIdList()).isEmpty();
    }

    @Test
    void testAddProductIdList() {
        PostProcessRequiredPaymentResponseDto dto = new PostProcessRequiredPaymentResponseDto();
        Long productId = 1L;

        dto.addProductIdList(productId);

        assertThat(dto.getProductIdList()).isNotNull();
        assertThat(dto.getProductIdList()).containsExactly(productId);
    }

    @Test
    void testAddMultipleProductIds() {
        PostProcessRequiredPaymentResponseDto dto = new PostProcessRequiredPaymentResponseDto();
        Long productId1 = 1L;
        Long productId2 = 2L;

        dto.addProductIdList(productId1);
        dto.addProductIdList(productId2);

        assertThat(dto.getProductIdList()).isNotNull();
        assertThat(dto.getProductIdList()).containsExactly(productId1, productId2);
    }

    @Test
    void testSetAndGetFieldsWithBuilder() {
        Long orderId = 100L;
        Long clientId = 200L;
        long amount = 5000L;
        String paymentMethodName = "Credit Card";

        PostProcessRequiredPaymentResponseDto dto = PostProcessRequiredPaymentResponseDto.builder()
            .orderId(orderId)
            .clientId(clientId)
            .amount(amount)
            .paymentMethodName(paymentMethodName)
            .build();

        assertThat(dto.getOrderId()).isEqualTo(orderId);
        assertThat(dto.getClientId()).isEqualTo(clientId);
        assertThat(dto.getAmount()).isEqualTo(amount);
        assertThat(dto.getPaymentMethodName()).isEqualTo(paymentMethodName);
    }
}
