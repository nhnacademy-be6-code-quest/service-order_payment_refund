package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundPolicyResponseDtoTest {

    @Test
    void testRefundPolicyResponseDtoWithAllArgsConstructor() {
        // Given
        Long refundPolicyId = 1L;
        String refundPolicyType = "Full Refund";
        Integer refundShippingFee = 100;

        // When
        RefundPolicyResponseDto dto = new RefundPolicyResponseDto(refundPolicyId, refundPolicyType, refundShippingFee);

        // Then
        assertNotNull(dto);
        assertEquals(refundPolicyId, dto.getRefundPolicyId());
        assertEquals(refundPolicyType, dto.getRefundPolicyType());
        assertEquals(refundShippingFee, dto.getRefundShippingFee());
    }

    @Test
    void testRefundPolicyResponseDtoWithDefaultConstructor() {
        // When
        RefundPolicyResponseDto dto = new RefundPolicyResponseDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getRefundPolicyId()); // Default value for Long
        assertNull(dto.getRefundPolicyType()); // Default value for String
        assertNull(dto.getRefundShippingFee()); // Default value for Integer
    }

    @Test
    void testSettersAndGetters() {
        // Given
        Long refundPolicyId = 2L;
        String refundPolicyType = "Partial Refund";
        Integer refundShippingFee = 50;

        // When
        RefundPolicyResponseDto dto = new RefundPolicyResponseDto(refundPolicyId, refundPolicyType, refundShippingFee);

        // Then
        assertEquals(refundPolicyId, dto.getRefundPolicyId());
        assertEquals(refundPolicyType, dto.getRefundPolicyType());
        assertEquals(refundShippingFee, dto.getRefundShippingFee());
    }
}
