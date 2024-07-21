package com.nhnacademy.orderpaymentrefund.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.message.ProductQuantityDesMessageDto;
import org.junit.jupiter.api.Test;

class ProductQuantityDesMessageDtoTest {
    @Test
    public void testNoArgsConstructor() {
        // Given
        // No given values since we are testing the no-arg constructor

        // When
        ProductQuantityDesMessageDto dto = new ProductQuantityDesMessageDto();

        // Then
        assertThat(dto.getProductId()).isNull();
        assertThat(dto.getQuantity()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        // Given
        Long productId = 12345L;
        Long quantity = 10L;

        // When
        ProductQuantityDesMessageDto dto = new ProductQuantityDesMessageDto(productId, quantity);

        // Then
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getQuantity()).isEqualTo(quantity);
    }

    @Test
    public void testSettersAndGetters() {
        // Given
        Long productId = 12345L;
        Long quantity = 10L;

        // When
        ProductQuantityDesMessageDto dto = new ProductQuantityDesMessageDto();
        dto.setProductId(productId);
        dto.setQuantity(quantity);

        // Then
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getQuantity()).isEqualTo(quantity);
    }
}
