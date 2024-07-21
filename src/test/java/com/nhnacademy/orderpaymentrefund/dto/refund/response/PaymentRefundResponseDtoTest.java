package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaymentRefundResponseDtoTest {

    @Test
    void testPaymentRefundResponseDtoWithDefaultConstructor() {
        // When
        PaymentRefundResponseDto dto = new PaymentRefundResponseDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getPaymentId()); // Default value for Long
        assertNull(dto.getTossPaymentKey()); // Default value for String
        assertNull(dto.getOrderStatus()); // Default value for String
    }

    @Test
    void testSettersAndGetters() {
        // Given
        PaymentRefundResponseDto dto = new PaymentRefundResponseDto();
        Long paymentId = 98765L;
        String tossPaymentKey = "TOSS98765";
        String orderStatus = "PROCESSED";

        // When
        dto.setPaymentId(paymentId);
        dto.setTossPaymentKey(tossPaymentKey);
        dto.setOrderStatus(orderStatus);

        // Then
        assertEquals(paymentId, dto.getPaymentId());
        assertEquals(tossPaymentKey, dto.getTossPaymentKey());
        assertEquals(orderStatus, dto.getOrderStatus());
    }
}
