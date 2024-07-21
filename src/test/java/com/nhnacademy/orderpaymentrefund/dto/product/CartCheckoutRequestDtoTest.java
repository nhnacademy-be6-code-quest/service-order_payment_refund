package com.nhnacademy.orderpaymentrefund.dto.product;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.List;

class CartCheckoutRequestDtoTest {

    @Test
    void testCartCheckoutRequestDtoCreationWithBuilder() {
        // Given
        Long clientId = 123L;

        // When
        CartCheckoutRequestDto dto = CartCheckoutRequestDto.builder()
            .clientId(clientId)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(clientId, dto.getClientId());
        assertNull(dto.getProductIdList()); // Should be null initially
    }

    @Test
    void testAddProductId() {
        // Given
        Long clientId = 123L;
        CartCheckoutRequestDto dto = new CartCheckoutRequestDto(clientId);

        // When
        dto.addProductId(456L);
        dto.addProductId(789L);

        // Then
        List<Long> productIdList = dto.getProductIdList();
        assertNotNull(productIdList);
        assertEquals(2, productIdList.size());
        assertTrue(productIdList.contains(456L));
        assertTrue(productIdList.contains(789L));
    }

    @Test
    void testAddProductIdWithNullList() {
        // Given
        CartCheckoutRequestDto dto = new CartCheckoutRequestDto();

        // When
        dto.addProductId(456L);

        // Then
        List<Long> productIdList = dto.getProductIdList();
        assertNotNull(productIdList);
        assertEquals(1, productIdList.size());
        assertTrue(productIdList.contains(456L));
    }

    @Test
    void testCartCheckoutRequestDtoWithNoArgsConstructor() {
        // Given
        CartCheckoutRequestDto dto = new CartCheckoutRequestDto();
        dto.setClientId(123L);

        // When
        dto.addProductId(456L);
        dto.addProductId(789L);

        // Then
        assertNotNull(dto.getClientId());
        assertEquals(123L, dto.getClientId());
        List<Long> productIdList = dto.getProductIdList();
        assertNotNull(productIdList);
        assertEquals(2, productIdList.size());
        assertTrue(productIdList.contains(456L));
        assertTrue(productIdList.contains(789L));
    }

    @Test
    void testCartCheckoutRequestDtoWithBuilderAndProductIds() {
        // Given
        Long clientId = 123L;

        // When
        CartCheckoutRequestDto dto = CartCheckoutRequestDto.builder()
            .clientId(clientId)
            .build();
        dto.addProductId(456L);
        dto.addProductId(789L);

        // Then
        assertNotNull(dto);
        assertEquals(clientId, dto.getClientId());
        List<Long> productIdList = dto.getProductIdList();
        assertNotNull(productIdList);
        assertEquals(2, productIdList.size());
        assertTrue(productIdList.contains(456L));
        assertTrue(productIdList.contains(789L));
    }
}
