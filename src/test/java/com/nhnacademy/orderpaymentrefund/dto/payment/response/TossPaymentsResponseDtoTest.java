package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TossPaymentsResponseDtoTest {

    @Test
    void testBuilder() {
        TossPaymentsResponseDto dto = TossPaymentsResponseDto.builder()
            .orderName("Test Order")
            .totalAmount(10000L)
            .method("카드")
            .paymentKey("test-key")
            .cardNumber("1234-5678-9012-3456")
            .accountNumber(null)
            .bank(null)
            .customerMobilePhone(null)
            .orderId("order-id-123")
            .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getOrderName()).isEqualTo("Test Order");
        assertThat(dto.getTotalAmount()).isEqualTo(10000L);
        assertThat(dto.getMethod()).isEqualTo("카드");
        assertThat(dto.getPaymentKey()).isEqualTo("test-key");
        assertThat(dto.getCardNumber()).isEqualTo("1234-5678-9012-3456");
        assertThat(dto.getAccountNumber()).isNull();
        assertThat(dto.getBank()).isNull();
        assertThat(dto.getCustomerMobilePhone()).isNull();
        assertThat(dto.getOrderId()).isEqualTo("order-id-123");
    }

    @Test
    void testSetters() {
        TossPaymentsResponseDto dto = new TossPaymentsResponseDto();
        dto.setCardNumber("1234-5678-9012-3456");
        dto.setAccountNumber("987654321");
        dto.setBank("001");
        dto.setCustomerMobilePhone("010-1234-5678");

        assertThat(dto.getCardNumber()).isEqualTo("1234-5678-9012-3456");
        assertThat(dto.getAccountNumber()).isEqualTo("987654321");
        assertThat(dto.getBank()).isEqualTo("001");
        assertThat(dto.getCustomerMobilePhone()).isEqualTo("010-1234-5678");
    }

    @Test
    void testDefaultValues() {
        TossPaymentsResponseDto dto = new TossPaymentsResponseDto();

        assertThat(dto).isNotNull();
        assertThat(dto.getOrderName()).isNull();
        assertThat(dto.getTotalAmount()).isZero();
        assertThat(dto.getMethod()).isNull();
        assertThat(dto.getPaymentKey()).isNull();
        assertThat(dto.getCardNumber()).isNull();
        assertThat(dto.getAccountNumber()).isNull();
        assertThat(dto.getBank()).isNull();
        assertThat(dto.getCustomerMobilePhone()).isNull();
        assertThat(dto.getOrderId()).isNull();
    }
}
