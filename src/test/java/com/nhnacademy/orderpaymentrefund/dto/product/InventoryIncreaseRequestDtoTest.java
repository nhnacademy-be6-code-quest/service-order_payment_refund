package com.nhnacademy.orderpaymentrefund.dto.product;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InventoryIncreaseRequestDtoTest {

    @Test
    void testInventoryIncreaseRequestDtoCreationWithAllArgsConstructor() {
        // Given
        Long productId = 100L;
        Long quantityToIncrease = 50L;

        // When
        InventoryIncreaseRequestDto dto = new InventoryIncreaseRequestDto(productId, quantityToIncrease);

        // Then
        assertNotNull(dto);
        assertEquals(productId, dto.getProductId());
        assertEquals(quantityToIncrease, dto.getQuantityToIncrease());
    }

    @Test
    void testInventoryIncreaseRequestDtoWithNoArgsConstructor() {
        // Given
        InventoryIncreaseRequestDto dto = new InventoryIncreaseRequestDto();
        dto.setProductId(100L);
        dto.setQuantityToIncrease(50L);

        // When & Then
        assertNotNull(dto.getProductId());
        assertEquals(100L, dto.getProductId());
        assertNotNull(dto.getQuantityToIncrease());
        assertEquals(50L, dto.getQuantityToIncrease());
    }

    @Test
    void testInventoryIncreaseRequestDtoWithNullValues() {
        // Given
        Long productId = null;
        Long quantityToIncrease = null;

        // When
        InventoryIncreaseRequestDto dto = new InventoryIncreaseRequestDto(productId, quantityToIncrease);

        // Then
        assertNotNull(dto);
        assertNull(dto.getProductId());
        assertNull(dto.getQuantityToIncrease());
    }
}
