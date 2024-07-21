package com.nhnacademy.orderpaymentrefund.dto.order.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import org.junit.jupiter.api.Test;

class FindNonClientOrderIdRequestDtoTest {
    @Test
    void testFindNonClientOrderIdRequestDto() {
        // Given
        String ordererName = "홍길동";
        String phoneNumber = "010-1234-5678";
        String email = "hong@example.com";

        // When
        FindNonClientOrderIdRequestDto dto = FindNonClientOrderIdRequestDto.builder()
            .ordererName(ordererName)
            .phoneNumber(phoneNumber)
            .email(email)
            .build();

        // Then
        assertThat(dto.ordererName()).isEqualTo(ordererName);
        assertThat(dto.phoneNumber()).isEqualTo(phoneNumber);
        assertThat(dto.email()).isEqualTo(email);
    }
}
