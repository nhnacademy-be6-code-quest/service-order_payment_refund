package com.nhnacademy.orderpaymentrefund.dto.message;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProductQuantityDesMessageDtoTest {

    @Test
    void testDefaultConstructor() {
        ProductQuantityDesMessageDto dto = new ProductQuantityDesMessageDto();
        assertThat(dto).isNotNull();
        assertThat(dto.getProductId()).isNull();
        assertThat(dto.getQuantity()).isNull();
    }

    @Test
    void testAllArgsConstructor() {
        ProductQuantityDesMessageDto dto = new ProductQuantityDesMessageDto(1L, 10L);
        assertThat(dto).isNotNull();
        assertThat(dto.getProductId()).isEqualTo(1L);
        assertThat(dto.getQuantity()).isEqualTo(10L);
    }

    @Test
    void testSettersAndGetters() {
        // Lombok @NoArgsConstructor와 @AllArgsConstructor가 제공하므로
        // 객체를 생성 후 필드 값이 올바르게 설정되는지 검증하는 테스트입니다.

        ProductQuantityDesMessageDto dto = new ProductQuantityDesMessageDto();
        dto.setProductId(1L);
        dto.setQuantity(10L);

        assertThat(dto.getProductId()).isEqualTo(1L);
        assertThat(dto.getQuantity()).isEqualTo(10L);
    }
}
