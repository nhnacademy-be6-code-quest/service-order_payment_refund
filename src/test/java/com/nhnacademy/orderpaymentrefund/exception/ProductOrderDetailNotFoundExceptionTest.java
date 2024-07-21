package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductOrderDetailNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        ProductOrderDetailNotFoundException exception = new ProductOrderDetailNotFoundException();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("productOrderDetail을 찾지 못했습니다.");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message for product order detail not found.";
        ProductOrderDetailNotFoundException exception = new ProductOrderDetailNotFoundException(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message for product order detail not found.";

        assertThatThrownBy(() -> {
            throw new ProductOrderDetailNotFoundException(customMessage);
        }).isInstanceOf(ProductOrderDetailNotFoundException.class)
            .hasMessage(customMessage);
    }
}
