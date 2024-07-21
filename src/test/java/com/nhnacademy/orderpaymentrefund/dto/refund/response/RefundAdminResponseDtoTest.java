package com.nhnacademy.orderpaymentrefund.dto.refund.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RefundAdminResponseDtoTest {

    @Test
    void testRefundAdminResponseDtoWithAllArgsConstructor() {
        // Given
        String refundPolicyType = "PolicyType1";
        Long refundAmount = 1500L;
        String refundDateTime = "2024-07-21T10:00:00";
        String refundDetailReason = "Product defect";

        // When
        RefundAdminResponseDto dto = new RefundAdminResponseDto(
            refundPolicyType, refundAmount, refundDateTime, refundDetailReason);

        // Then
        assertNotNull(dto);
        assertEquals(refundPolicyType, dto.getRefundPolicyType());
        assertEquals(refundAmount, dto.getRefundAmount());
        assertEquals(refundDateTime, dto.getRefundDateTime());
        assertEquals(refundDetailReason, dto.getRefundDetailReason());
    }

    @Test
    void testRefundAdminResponseDtoWithDefaultConstructor() {
        // When
        RefundAdminResponseDto dto = new RefundAdminResponseDto();

        // Then
        assertNotNull(dto);
        assertNull(dto.getRefundPolicyType()); // Default value for String
        assertNull(dto.getRefundAmount()); // Default value for Long
        assertNull(dto.getRefundDateTime()); // Default value for String
        assertNull(dto.getRefundDetailReason()); // Default value for String
    }

    @Test
    void testSettersAndGetters() {
        // Given
        RefundAdminResponseDto dto = new RefundAdminResponseDto();
        String refundPolicyType = "PolicyType2";
        Long refundAmount = 2000L;
        String refundDateTime = "2024-07-22T15:30:00";
        String refundDetailReason = "Service issue";

        // When
        dto.setRefundPolicyType(refundPolicyType);
        dto.setRefundAmount(refundAmount);
        dto.setRefundDateTime(refundDateTime);
        dto.setRefundDetailReason(refundDetailReason);

        // Then
        assertEquals(refundPolicyType, dto.getRefundPolicyType());
        assertEquals(refundAmount, dto.getRefundAmount());
        assertEquals(refundDateTime, dto.getRefundDateTime());
        assertEquals(refundDetailReason, dto.getRefundDetailReason());
    }
}
