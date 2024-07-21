package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentNotCompletedExceptionTest {

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Payment has not been completed.";
        PaymentNotCompletedException exception = new PaymentNotCompletedException(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Payment has not been completed.";
        assertThatThrownBy(() -> {
            throw new PaymentNotCompletedException(customMessage);
        }).isInstanceOf(PaymentNotCompletedException.class)
            .hasMessage(customMessage);
    }
}
