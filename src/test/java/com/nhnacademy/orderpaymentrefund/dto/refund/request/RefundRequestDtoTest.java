package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundRequestDtoTest {

    @Test
    void testRefundRequestDtoCreationAndSetters() {
        // Given
        long orderId = 12345L;
        String refundDetailReason = "Product was defective";
        long refundPolicyId = 67890L;

        // When
        RefundRequestDto dto = new RefundRequestDto();
        dto.setOrderId(orderId);
        dto.setRefundDetailReason(refundDetailReason);
        dto.setRefundPolicyId(refundPolicyId);

        // Then
        assertEquals(orderId, dto.getOrderId());
        assertEquals(refundDetailReason, dto.getRefundDetailReason());
        assertEquals(refundPolicyId, dto.getRefundPolicyId());
    }

    @Test
    void testRefundRequestDtoWithConstructor() {
        // Given
        long orderId = 12345L;
        String refundDetailReason = "Order mistake";
        long refundPolicyId = 67890L;

        // When
        RefundRequestDto dto = new RefundRequestDto();
        dto.setOrderId(orderId);
        dto.setRefundDetailReason(refundDetailReason);
        dto.setRefundPolicyId(refundPolicyId);

        // Then
        assertNotNull(dto);
        assertEquals(orderId, dto.getOrderId());
        assertEquals(refundDetailReason, dto.getRefundDetailReason());
        assertEquals(refundPolicyId, dto.getRefundPolicyId());
    }

    @Test
    void testRefundRequestDtoDefaultValues() {
        // When
        RefundRequestDto dto = new RefundRequestDto();

        // Then
        assertEquals(0L, dto.getOrderId()); // Default value for long
        assertNull(dto.getRefundDetailReason()); // Default value for String
        assertEquals(0L, dto.getRefundPolicyId()); // Default value for long
    }
}
