package com.nhnacademy.orderpaymentrefund.order.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import org.junit.jupiter.api.Test;

class FindNonClientOrderPasswordRequestDtoTest {
    @Test
    public void testFindNonClientOrderPasswordRequestDto() {
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
        assertThat(dto.orderId()).isEqualTo(orderId);
        assertThat(dto.ordererName()).isEqualTo(ordererName);
        assertThat(dto.phoneNumber()).isEqualTo(phoneNumber);
        assertThat(dto.email()).isEqualTo(email);
    }
}
