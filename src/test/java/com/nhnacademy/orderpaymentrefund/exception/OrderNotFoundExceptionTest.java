package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        OrderNotFoundException exception = new OrderNotFoundException();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("주문을 찾을 수 없습니다.");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        OrderNotFoundException exception = new OrderNotFoundException(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message";
        assertThatThrownBy(() -> {
            throw new OrderNotFoundException(customMessage);
        }).isInstanceOf(OrderNotFoundException.class)
            .hasMessage(customMessage);
    }

    @Test
    void testDefaultMessageExceptionThrown() {
        assertThatThrownBy(() -> {
            throw new OrderNotFoundException();
        }).isInstanceOf(OrderNotFoundException.class)
            .hasMessage("주문을 찾을 수 없습니다.");
    }
}
