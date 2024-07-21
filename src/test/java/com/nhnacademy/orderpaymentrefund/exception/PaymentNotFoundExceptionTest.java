package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        PaymentNotFoundException exception = new PaymentNotFoundException();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("결제 정보를 찾을 수 없습니다.");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message for payment not found.";
        PaymentNotFoundException exception = new PaymentNotFoundException(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message for payment not found.";

        assertThatThrownBy(() -> {
            throw new PaymentNotFoundException(customMessage);
        }).isInstanceOf(PaymentNotFoundException.class)
            .hasMessage(customMessage);
    }
}
