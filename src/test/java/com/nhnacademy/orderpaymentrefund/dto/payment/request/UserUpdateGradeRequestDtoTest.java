package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserUpdateGradeRequestDtoTest {

    @Test
    void testBuilder() {
        Long clientId = 123L;

        UserUpdateGradeRequestDto dto = UserUpdateGradeRequestDto.builder()
            .clientId(clientId)
            .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isEqualTo(clientId);
    }

    @Test
    void testDefaultConstructor() {
        UserUpdateGradeRequestDto dto = new UserUpdateGradeRequestDto(null);

        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isNull(); // 기본값은 null
    }
}
