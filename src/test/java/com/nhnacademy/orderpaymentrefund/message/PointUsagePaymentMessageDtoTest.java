package com.nhnacademy.orderpaymentrefund.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.message.PointUsagePaymentMessageDto;
import org.junit.jupiter.api.Test;

class PointUsagePaymentMessageDtoTest {
    @Test
    public void testNoArgsConstructor() {
        // Given
        // No given values since we are testing the no-arg constructor

        // When
        PointUsagePaymentMessageDto dto = new PointUsagePaymentMessageDto();

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
        PointUsagePaymentMessageDto dto = new PointUsagePaymentMessageDto(clientId, pointUsagePayment);

        // Then
        assertThat(dto.getClientId()).isEqualTo(clientId);
        assertThat(dto.getPointUsagePayment()).isEqualTo(pointUsagePayment);
    }

    @Test
    public void testBuilder() {
        // Given
        Long clientId = 12345L;
        Long pointUsagePayment = 500L;

        // When
        PointUsagePaymentMessageDto dto = PointUsagePaymentMessageDto.builder()
            .clientId(clientId)
            .pointUsagePayment(pointUsagePayment)
            .build();

        // Then
        assertThat(dto.getClientId()).isEqualTo(clientId);
        assertThat(dto.getPointUsagePayment()).isEqualTo(pointUsagePayment);
    }

}
