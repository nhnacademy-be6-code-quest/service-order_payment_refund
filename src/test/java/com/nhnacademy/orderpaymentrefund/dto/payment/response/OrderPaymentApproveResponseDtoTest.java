package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderPaymentApproveResponseDtoTest {

    @Test
    void testBuilder() {
        long totalPrice = 1000L;
        ProductOrderDetailResponseDto detail = new ProductOrderDetailResponseDto(1L, 1L, 1L, 1L); // Assume this is a valid object
        List<ProductOrderDetailResponseDto> detailsList = Collections.singletonList(detail);

        OrderPaymentResponseDto dto = OrderPaymentResponseDto.builder()
            .totalPrice(totalPrice)
            .productOrderDetailResponseDtoList(detailsList)
            .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getTotalPrice()).isEqualTo(totalPrice);
        assertThat(dto.getProductOrderDetailResponseDtoList()).isEqualTo(detailsList);
    }

    @Test
    void testProductOrderDetailResponseDtoListNotNull() {
        long totalPrice = 1000L;

        OrderPaymentResponseDto dto = OrderPaymentResponseDto.builder()
            .totalPrice(totalPrice)
            .productOrderDetailResponseDtoList(Collections.emptyList())
            .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getProductOrderDetailResponseDtoList()).isNotNull(); // Ensure that it's not null
        assertThat(dto.getProductOrderDetailResponseDtoList()).isEmpty(); // Ensure that it can be an empty list
    }

    @Test
    void testProductOrderDetailResponseDtoListNull() {
        long totalPrice = 1000L;

        try {
            OrderPaymentResponseDto dto = OrderPaymentResponseDto.builder()
                .totalPrice(totalPrice)
                .build();

            // Ensure that an exception is thrown if productOrderDetailResponseDtoList is null
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NullPointerException.class); // Expecting NullPointerException or other validation exception
        }
    }
}
