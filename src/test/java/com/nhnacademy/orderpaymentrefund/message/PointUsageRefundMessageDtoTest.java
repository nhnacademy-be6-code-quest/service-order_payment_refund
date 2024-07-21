package com.nhnacademy.orderpaymentrefund.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.message.PointUsageRefundMessageDto;
import org.junit.jupiter.api.Test;

class PointUsageRefundMessageDtoTest {

    @Test
    public void testNoArgsConstructor() {
        // Given
        // No given values since we are testing the no-arg constructor

        // When
        PointUsageRefundMessageDto dto = new PointUsageRefundMessageDto();

        // Then
        assertThat(dto.getClientId()).isNull();
        assertThat(dto.getPointUsagePayment()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Given
        Long clientId = 12345L;
        Long pointUsagePayment = 500L;

        // When
        PointUsageRefundMessageDto dto = new PointUsageRefundMessageDto(clientId,
            pointUsagePayment);

        // Then
        assertThat(dto.getClientId()).isEqualTo(clientId);
        assertThat(dto.getPointUsagePayment()).isEqualTo(pointUsagePayment);
    }
}
