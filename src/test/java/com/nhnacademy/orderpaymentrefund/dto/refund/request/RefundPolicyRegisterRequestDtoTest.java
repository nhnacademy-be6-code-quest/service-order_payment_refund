package com.nhnacademy.orderpaymentrefund.dto.refund.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundPolicyRegisterRequestDtoTest {

    @Test
    void testRefundPolicyRegisterRequestDtoWithBuilder() {
        // Given
        String refundPolicyType = "Standard";
        Integer refundShippingFee = 10;

        // When
        RefundPolicyRegisterRequestDto dto = RefundPolicyRegisterRequestDto.builder()
            .refundPolicyType(refundPolicyType)
            .refundShippingFee(refundShippingFee)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(refundPolicyType, dto.getRefundPolicyType());
        assertEquals(refundShippingFee, dto.getRefundShippingFee());
    }

    @Test
    void testRefundPolicyRegisterRequestDtoWithNullValues() {
        // Given
        String refundPolicyType = null;
        Integer refundShippingFee = null;

        // When
        RefundPolicyRegisterRequestDto dto = RefundPolicyRegisterRequestDto.builder()
            .refundPolicyType(refundPolicyType)
            .refundShippingFee(refundShippingFee)
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getRefundPolicyType());
        assertNull(dto.getRefundShippingFee());
    }
}
