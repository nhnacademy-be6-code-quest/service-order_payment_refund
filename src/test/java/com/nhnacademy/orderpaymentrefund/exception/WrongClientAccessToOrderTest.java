package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WrongClientAccessToOrderTest {

    @Test
    void testDefaultConstructor() {
        WrongClientAccessToOrder exception = new WrongClientAccessToOrder();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("본인의 Order 데이터에 접근하지 않았습니다.");
    }

    @Test
    void testConstructorWithMessage() {
        String customMessage = "Custom error message for wrong client access to order.";
        WrongClientAccessToOrder exception = new WrongClientAccessToOrder(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message for wrong client access to order.";

        assertThatThrownBy(() -> {
            throw new WrongClientAccessToOrder(customMessage);
        }).isInstanceOf(WrongClientAccessToOrder.class)
            .hasMessage(customMessage);
    }
}
