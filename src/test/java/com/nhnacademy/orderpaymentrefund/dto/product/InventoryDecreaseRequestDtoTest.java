package com.nhnacademy.orderpaymentrefund.dto.product;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

class InventoryDecreaseRequestDtoTest {

    @Test
    void testInventoryDecreaseRequestDtoCreationWithBuilder() {
        // Given
        Long orderId = 123L;
        Map<Long, Long> decreaseInfo = new HashMap<>();
        decreaseInfo.put(1L, 10L);
        decreaseInfo.put(2L, 5L);

        // When
        InventoryDecreaseRequestDto dto = InventoryDecreaseRequestDto.builder()
            .orderId(orderId)
            .decreaseInfo(decreaseInfo)
            .build();

        // Then
        assertNotNull(dto);
        assertEquals(orderId, dto.getOrderId());
        assertEquals(decreaseInfo, dto.getDecreaseInfo());
    }

    @Test
    void testInventoryDecreaseRequestDtoWithAllArgsConstructor() {
        // Given
        Long orderId = 123L;
        Map<Long, Long> decreaseInfo = new HashMap<>();
        decreaseInfo.put(1L, 10L);
        decreaseInfo.put(2L, 5L);

        // When
        InventoryDecreaseRequestDto dto = new InventoryDecreaseRequestDto(orderId, decreaseInfo);

        // Then
        assertNotNull(dto);
        assertEquals(orderId, dto.getOrderId());
        assertEquals(decreaseInfo, dto.getDecreaseInfo());
    }

    @Test
    void testInventoryDecreaseRequestDtoWithNoArgsConstructor() {
        // Given
        InventoryDecreaseRequestDto dto = new InventoryDecreaseRequestDto();
        dto.setOrderId(123L);
        Map<Long, Long> decreaseInfo = new HashMap<>();
        decreaseInfo.put(1L, 10L);
        decreaseInfo.put(2L, 5L);
        dto.setDecreaseInfo(decreaseInfo);

        // When & Then
        assertNotNull(dto.getOrderId());
        assertEquals(123L, dto.getOrderId());
        assertNotNull(dto.getDecreaseInfo());
        assertEquals(decreaseInfo, dto.getDecreaseInfo());
    }

    @Test
    void testInventoryDecreaseRequestDtoWithNullValues() {
        // Given
        Long orderId = null;
        Map<Long, Long> decreaseInfo = null;

        // When
        InventoryDecreaseRequestDto dto = InventoryDecreaseRequestDto.builder()
            .orderId(orderId)
            .decreaseInfo(decreaseInfo)
            .build();

        // Then
        assertNotNull(dto);
        assertNull(dto.getOrderId());
        assertNull(dto.getDecreaseInfo());
    }

    @Test
    void testAddDecreaseInfo() {
        // Given
        Long orderId = 123L;
        InventoryDecreaseRequestDto dto = new InventoryDecreaseRequestDto();
        dto.setOrderId(orderId);

        // When
        Map<Long, Long> decreaseInfo = new HashMap<>();
        decreaseInfo.put(1L, 10L);
        decreaseInfo.put(2L, 5L);
        dto.setDecreaseInfo(decreaseInfo);

        // Then
        assertNotNull(dto.getDecreaseInfo());
        assertEquals(2, dto.getDecreaseInfo().size());
        assertEquals(10L, dto.getDecreaseInfo().get(1L));
        assertEquals(5L, dto.getDecreaseInfo().get(2L));
    }
}
