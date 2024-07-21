package com.nhnacademy.orderpaymentrefund.dto.order.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class FindNonClientOrderIdInfoResponseDtoTest {
    @Test
    void testFindNonClientOrderIdInfoResponseDtoBuilder() {

        long orderId = 12345L;
        LocalDateTime orderDateTime = LocalDateTime.of(2024, 7, 21, 12, 34, 56);

        // When
        FindNonClientOrderIdInfoResponseDto dto = FindNonClientOrderIdInfoResponseDto.builder()
            .orderId(orderId)
            .orderDateTime(orderDateTime)
            .build();

        // Then
        assertThat(dto.orderId()).isEqualTo(orderId);
        assertThat(dto.orderDateTime()).isEqualTo(orderDateTime);
    }

    @Test
    void testFindNonClientOrderIdInfoResponseDtoConstructor() {
        // Given
        long orderId = 12345L;
        LocalDateTime orderDateTime = LocalDateTime.of(2024, 7, 21, 12, 34, 56);

        // When
        FindNonClientOrderIdInfoResponseDto dto = new FindNonClientOrderIdInfoResponseDto(orderId, orderDateTime);

        // Then
        assertThat(dto.orderId()).isEqualTo(orderId);
        assertThat(dto.orderDateTime()).isEqualTo(orderDateTime);
    }
}
