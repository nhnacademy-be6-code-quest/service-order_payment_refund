package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundRegisterRequestDtoTest {

    @Test
    void testRefundRegisterRequestDtoWithBuilder() {
        // Given
        String cancelReason = "Product damaged";
        long paymentId = 12345L;
        String orderStatus = "Pending";
        long orderId = 67890L;

        // When
        RefundRegisterRequestDto dto = RefundRegisterRequestDto.builder()
            .cancelReason(cancelReason)
            .paymentId(paymentId)
            .orderStatus(orderStatus)
            .orderId(orderId)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(cancelReason, dto.getCancelReason());
        assertEquals(paymentId, dto.getPaymentId());
        assertEquals(orderStatus, dto.getOrderStatus());
        assertEquals(orderId, dto.getOrderId());
    }

    @Test
    void testRefundRegisterRequestDtoWithDefaultValues() {
        // When
        RefundRegisterRequestDto dto = RefundRegisterRequestDto.builder()
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getCancelReason());
        assertEquals(0L, dto.getPaymentId()); // Default value for long
        assertNull(dto.getOrderStatus());
        assertEquals(0L, dto.getOrderId()); // Default value for long
    }

    @Test
    void testRefundRegisterRequestDtoWithPartialBuilder() {
        // Given
        String cancelReason = "Customer request";

        // When
        RefundRegisterRequestDto dto = RefundRegisterRequestDto.builder()
            .cancelReason(cancelReason)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(cancelReason, dto.getCancelReason());
        assertEquals(0L, dto.getPaymentId()); // Default value for long
        assertNull(dto.getOrderStatus());
        assertEquals(0L, dto.getOrderId()); // Default value for long
    }
}
