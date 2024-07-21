package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderPasswordNotCorrectTest {

    @Test
    void testDefaultConstructor() {
        OrderPasswordNotCorrect exception = new OrderPasswordNotCorrect();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("비회원 주문 비밀번호가 일치하지 않습니다");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        OrderPasswordNotCorrect exception = new OrderPasswordNotCorrect(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message";
        assertThatThrownBy(() -> {
            throw new OrderPasswordNotCorrect(customMessage);
        }).isInstanceOf(OrderPasswordNotCorrect.class)
            .hasMessage(customMessage);
    }

    @Test
    void testDefaultMessageExceptionThrown() {
        assertThatThrownBy(() -> {
            throw new OrderPasswordNotCorrect();
        }).isInstanceOf(OrderPasswordNotCorrect.class)
            .hasMessage("비회원 주문 비밀번호가 일치하지 않습니다");
    }
}
