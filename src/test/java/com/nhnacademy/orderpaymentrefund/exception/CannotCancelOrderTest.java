package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CannotCancelOrderTest {

    @Test
    void testDefaultConstructor() {
        CannotCancelOrder exception = new CannotCancelOrder();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("주문 취소에 실패하였습니다");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        CannotCancelOrder exception = new CannotCancelOrder(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        assertThatThrownBy(() -> {
            throw new CannotCancelOrder();
        }).isInstanceOf(CannotCancelOrder.class)
            .hasMessage("주문 취소에 실패하였습니다");

        String customMessage = "Custom error message";
        assertThatThrownBy(() -> {
            throw new CannotCancelOrder(customMessage);
        }).isInstanceOf(CannotCancelOrder.class)
            .hasMessage(customMessage);
    }
}
