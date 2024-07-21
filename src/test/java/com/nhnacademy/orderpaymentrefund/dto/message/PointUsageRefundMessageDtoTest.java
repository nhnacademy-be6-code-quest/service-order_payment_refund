package com.nhnacademy.orderpaymentrefund.dto.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PointUsageRefundMessageDtoTest {

    @Test
    void testDefaultConstructor() {
        PointUsageRefundMessageDto dto = new PointUsageRefundMessageDto();
        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isNull();
        assertThat(dto.getPointUsagePayment()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        PointUsageRefundMessageDto dto = new PointUsageRefundMessageDto(1L, 500L);
        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isEqualTo(1L);
        assertThat(dto.getPointUsagePayment()).isEqualTo(500L);
    }

    @Test
    void testSettersAndGetters() {
        // Lombok @NoArgsConstructor와 @AllArgsConstructor가 제공하므로
        // 객체를 생성 후 필드 값이 올바르게 설정되는지 검증하는 테스트입니다.

        PointUsageRefundMessageDto dto = new PointUsageRefundMessageDto(1L, 500L);

        assertThat(dto.getClientId()).isEqualTo(1L);
        assertThat(dto.getPointUsagePayment()).isEqualTo(500L);
    }
}
