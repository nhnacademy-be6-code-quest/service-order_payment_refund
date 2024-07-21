package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PaymentCancelRequestDtoTest {

    @Test
    void testPaymentCancelRequestDtoWithNoArgsConstructor() {
        // Given
        PaymentCancelRequestDto dto = new PaymentCancelRequestDto();

        // When
        dto.setOrderStatus("Cancelled");
        dto.setOrderId(123L);
        dto.setCancelReason("Customer request");

        // Then
        assertNotNull(dto);
        assertEquals("Cancelled", dto.getOrderStatus());
        assertEquals(123L, dto.getOrderId());
        assertEquals("Customer request", dto.getCancelReason());
    }

    @Test
    void testPaymentCancelRequestDtoFieldValues() {
        // Given
        String orderStatus = "Cancelled";
        long orderId = 123L;
        String cancelReason = "Customer request";

        // When
        PaymentCancelRequestDto dto = new PaymentCancelRequestDto();
        dto.setOrderStatus(orderStatus);
        dto.setOrderId(orderId);
        dto.setCancelReason(cancelReason);

        // Then
        assertEquals(orderStatus, dto.getOrderStatus());
        assertEquals(orderId, dto.getOrderId());
        assertEquals(cancelReason, dto.getCancelReason());
    }

    @Test
    void testPaymentCancelRequestDtoDefaultValues() {
        // Given
        PaymentCancelRequestDto dto = new PaymentCancelRequestDto();

        // When & Then
        assertNull(dto.getOrderStatus());
        assertEquals(0L, dto.getOrderId()); // default value for long
        assertNull(dto.getCancelReason());
    }
}
