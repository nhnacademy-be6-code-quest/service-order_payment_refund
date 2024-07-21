package com.nhnacademy.orderpaymentrefund.order.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import org.junit.jupiter.api.Test;

public class ProductOrderDetailResponseDtoTest {

    @Test
    public void testProductOrderDetailResponseDtoBuilder() {
        // Given
        Long productOrderDetailId = 1L;
        Long orderId = 1L;
        Long productId = 1L;
        Long quantity = 2L;
        Long pricePerProduct = 1000L;
        String productName = "Product Name";

        // When
        ProductOrderDetailResponseDto dto = ProductOrderDetailResponseDto.builder()
            .productOrderDetailId(productOrderDetailId)
            .orderId(orderId)
            .productId(productId)
            .quantity(quantity)
            .pricePerProduct(pricePerProduct)
            .productName(productName)
            .build();

        // Then
        assertThat(dto.getProductOrderDetailId()).isEqualTo(productOrderDetailId);
        assertThat(dto.getOrderId()).isEqualTo(orderId);
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getQuantity()).isEqualTo(quantity);
        assertThat(dto.getPricePerProduct()).isEqualTo(pricePerProduct);
        assertThat(dto.getProductName()).isEqualTo(productName);
    }

    @Test
    public void testProductOrderDetailResponseDtoNoArgsConstructor() {
        // When
        ProductOrderDetailResponseDto dto = new ProductOrderDetailResponseDto();

        // Then
        assertThat(dto.getProductOrderDetailId()).isNull();
        assertThat(dto.getOrderId()).isNull();
        assertThat(dto.getProductId()).isNull();
        assertThat(dto.getQuantity()).isNull();
        assertThat(dto.getPricePerProduct()).isNull();
        assertThat(dto.getProductName()).isNull();
    }

    @Test
    public void testProductOrderDetailResponseDtoAllArgsConstructor() {
        // Given
        Long productOrderDetailId = 1L;
        Long orderId = 1L;
        Long productId = 1L;
        Long quantity = 2L;
        Long pricePerProduct = 1000L;
        String productName = "Product Name";

        // When
        ProductOrderDetailResponseDto dto = new ProductOrderDetailResponseDto(
            productOrderDetailId, orderId, productId, quantity, pricePerProduct, productName);

        // Then
        assertThat(dto.getProductOrderDetailId()).isEqualTo(productOrderDetailId);
        assertThat(dto.getOrderId()).isEqualTo(orderId);
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getQuantity()).isEqualTo(quantity);
        assertThat(dto.getPricePerProduct()).isEqualTo(pricePerProduct);
        assertThat(dto.getProductName()).isEqualTo(productName);
    }
}

