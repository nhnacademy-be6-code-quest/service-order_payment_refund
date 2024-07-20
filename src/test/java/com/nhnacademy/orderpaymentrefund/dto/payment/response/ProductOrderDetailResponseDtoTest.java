package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProductOrderDetailResponseDtoTest {

    @Test
    void testBuilder() {
        long productId = 100L;
        long quantity = 2L;
        long pricePerProduct = 5000L;
        long productCategoryId = 10L;

        ProductOrderDetailResponseDto dto = ProductOrderDetailResponseDto.builder()
            .productId(productId)
            .quantity(quantity)
            .pricePerProduct(pricePerProduct)
            .productCategoryId(productCategoryId)
            .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getQuantity()).isEqualTo(quantity);
        assertThat(dto.getPricePerProduct()).isEqualTo(pricePerProduct);
        assertThat(dto.getProductCategoryId()).isEqualTo(productCategoryId);
    }

    @Test
    void testDefaultValues() {
        ProductOrderDetailResponseDto dto = ProductOrderDetailResponseDto.builder().build();

        assertThat(dto).isNotNull();
        assertThat(dto.getProductId()).isEqualTo(0L);
        assertThat(dto.getQuantity()).isEqualTo(0L);
        assertThat(dto.getPricePerProduct()).isEqualTo(0L);
        assertThat(dto.getProductCategoryId()).isEqualTo(0L);
    }
}
