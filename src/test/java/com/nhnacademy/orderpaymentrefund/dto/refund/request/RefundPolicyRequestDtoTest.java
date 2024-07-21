package com.nhnacademy.orderpaymentrefund.dto.refund.request;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundPolicyRequestDtoTest {

    @Test
    void testRefundPolicyRequestDtoWithBuilder() {
        // Given
        String refundAndCancelPolicyReason = "Product defect";
        String refundAndCancelPolicyType = "Full refund";
        int refundShippingFee = 20;

        // When
        RefundPolicyRequestDto dto = RefundPolicyRequestDto.builder()
            .refundAndCancelPolicyReason(refundAndCancelPolicyReason)
            .refundAndCancelPolicyType(refundAndCancelPolicyType)
            .refundShippingFee(refundShippingFee)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(refundAndCancelPolicyReason, dto.getRefundAndCancelPolicyReason());
        assertEquals(refundAndCancelPolicyType, dto.getRefundAndCancelPolicyType());
        assertEquals(refundShippingFee, dto.getRefundShippingFee());
    }

    @Test
    void testRefundPolicyRequestDtoWithDefaultValues() {
        // When
        RefundPolicyRequestDto dto = RefundPolicyRequestDto.builder()
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getRefundAndCancelPolicyReason());
        assertNull(dto.getRefundAndCancelPolicyType());
        assertEquals(0, dto.getRefundShippingFee()); // Default value for int
    }

    @Test
    void testRefundPolicyRequestDtoWithPartialBuilder() {
        // Given
        String refundAndCancelPolicyReason = "Order mistake";

        // When
        RefundPolicyRequestDto dto = RefundPolicyRequestDto.builder()
            .refundAndCancelPolicyReason(refundAndCancelPolicyReason)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(refundAndCancelPolicyReason, dto.getRefundAndCancelPolicyReason());
        assertNull(dto.getRefundAndCancelPolicyType());
        assertEquals(0, dto.getRefundShippingFee()); // Default value for int
    }
}
