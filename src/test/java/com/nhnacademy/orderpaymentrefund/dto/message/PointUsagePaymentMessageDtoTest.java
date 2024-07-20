package com.nhnacademy.orderpaymentrefund.dto.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PointUsagePaymentMessageDtoTest {

    @Test
    void testDefaultConstructor() {
        PointUsagePaymentMessageDto dto = new PointUsagePaymentMessageDto();
        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isNull();
        assertThat(dto.getPointUsagePayment()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        PointUsagePaymentMessageDto dto = new PointUsagePaymentMessageDto(1L, 500L);
        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isEqualTo(1L);
        assertThat(dto.getPointUsagePayment()).isEqualTo(500L);
    }

    @Test
    void testBuilder() {
        PointUsagePaymentMessageDto dto = PointUsagePaymentMessageDto.builder()
            .clientId(1L)
            .pointUsagePayment(500L)
            .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isEqualTo(1L);
        assertThat(dto.getPointUsagePayment()).isEqualTo(500L);
    }

    @Test
    void testSettersAndGetters() {
        // Lombok @Builder 생성자는 setter 메서드를 제공하지 않지만,
        // 객체를 생성 후 필드 값이 올바르게 설정되는지 검증하는 테스트를 포함합니다.

        PointUsagePaymentMessageDto dto = PointUsagePaymentMessageDto.builder()
            .clientId(1L)
            .pointUsagePayment(500L)
            .build();

        assertThat(dto.getClientId()).isEqualTo(1L);
        assertThat(dto.getPointUsagePayment()).isEqualTo(500L);
    }
}
