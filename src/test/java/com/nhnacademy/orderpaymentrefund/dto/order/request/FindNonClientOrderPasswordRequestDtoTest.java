package com.nhnacademy.orderpaymentrefund.dto.order.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import org.junit.jupiter.api.Test;

class FindNonClientOrderPasswordRequestDtoTest {
    @Test
    void testFindNonClientOrderPasswordRequestDto() {
        // Given
        long orderId = 123456L;
        String ordererName = "홍길동";
        String phoneNumber = "010-1234-5678";
        String email = "hong@example.com";

        // When
        FindNonClientOrderPasswordRequestDto dto = FindNonClientOrderPasswordRequestDto.builder()
            .orderId(orderId)
            .ordererName(ordererName)
            .phoneNumber(phoneNumber)
            .email(email)
            .build();

        // Then
        assertThat(dto.getOrderId()).isEqualTo(orderId);
        assertThat(dto.getOrdererName()).isEqualTo(ordererName);
        assertThat(dto.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(dto.getEmail()).isEqualTo(email);
    }
}
