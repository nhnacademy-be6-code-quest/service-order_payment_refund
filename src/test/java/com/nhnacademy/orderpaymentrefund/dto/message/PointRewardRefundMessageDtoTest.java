package com.nhnacademy.orderpaymentrefund.dto.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PointRewardRefundMessageDtoTest {

    @Test
    void testDefaultConstructor() {
        PointRewardRefundMessageDto dto = new PointRewardRefundMessageDto();
        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isNull();
        assertThat(dto.getPayment()).isNull();
        assertThat(dto.getDiscountAmountByPoint()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        PointRewardRefundMessageDto dto = new PointRewardRefundMessageDto(1L, 1000L, 200L);
        assertThat(dto).isNotNull();
        assertThat(dto.getClientId()).isEqualTo(1L);
        assertThat(dto.getPayment()).isEqualTo(1000L);
        assertThat(dto.getDiscountAmountByPoint()).isEqualTo(200L);
    }

    @Test
    void testSettersAndGetters() {
        PointRewardRefundMessageDto dto = new PointRewardRefundMessageDto(1L, 1000L, 200L);

        assertThat(dto.getClientId()).isEqualTo(1L);
        assertThat(dto.getPayment()).isEqualTo(1000L);
        assertThat(dto.getDiscountAmountByPoint()).isEqualTo(200L);
    }
}
