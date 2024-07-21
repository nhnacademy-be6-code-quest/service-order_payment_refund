package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CannotCancelPaymentCancelTest {

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Payment cancellation failed";
        CannotCancelPaymentCancel exception = new CannotCancelPaymentCancel(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Payment cancellation failed";
        assertThatThrownBy(() -> {
            throw new CannotCancelPaymentCancel(customMessage);
        }).isInstanceOf(CannotCancelPaymentCancel.class)
            .hasMessage(customMessage);
    }
}
