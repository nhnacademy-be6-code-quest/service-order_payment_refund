package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RefundImpossibleExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Refund is not possible for this order.";
        RefundImpossibleException exception = new RefundImpossibleException(message);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void testExceptionThrown() {
        String message = "Refund is not possible for this order.";

        assertThatThrownBy(() -> {
            throw new RefundImpossibleException(message);
        }).isInstanceOf(RefundImpossibleException.class)
            .hasMessage(message);
    }
}
