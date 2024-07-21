package com.nhnacademy.orderpaymentrefund.dto.refund.response;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaymentCancelResponseDtoTest {

    @Test
    void testPaymentCancelResponseDtoWithAllArgsConstructor() {
        // Given
        Long paymentId = 12345L;
        String tossPaymentKey = "TOSS123456";
        String orderStatus = "CANCELLED";

        // When
        PaymentCancelResponseDto dto = new PaymentCancelResponseDto(paymentId, tossPaymentKey, orderStatus);

        // Then
        assertNotNull(dto);
        assertEquals(paymentId, dto.getPaymentId());
        assertEquals(tossPaymentKey, dto.getTossPaymentKey());
        assertEquals(orderStatus, dto.getOrderStatus());
    }

    @Test
    void testPaymentCancelResponseDtoWithDefaultConstructor() {
        // When
        PaymentCancelResponseDto dto = new PaymentCancelResponseDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getPaymentId()); // Default value for Long
        assertNull(dto.getTossPaymentKey()); // Default value for String
        assertNull(dto.getOrderStatus()); // Default value for String
    }

    @Test
    void testSetters() {
        // Given

        Long paymentId = 67890L;
        String tossPaymentKey = "TOSS67890";
        String orderStatus = "REFUNDED";
        PaymentCancelResponseDto dto = new PaymentCancelResponseDto(paymentId, tossPaymentKey, orderStatus);

        // Then
        assertEquals(paymentId, dto.getPaymentId());
        assertEquals(tossPaymentKey, dto.getTossPaymentKey());
        assertEquals(orderStatus, dto.getOrderStatus());
    }
}
