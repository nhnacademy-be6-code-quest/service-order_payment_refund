package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShippingPolicyNotFoundExceptionTest {

    @Test
    void testDefaultConstructor() {
        ShippingPolicyNotFoundException exception = new ShippingPolicyNotFoundException();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("배송 정책을 찾을 수 없습니다.");
    }

    @Test
    void testConstructorWithMessage() {
        String message = "Custom error message for shipping policy not found.";
        ShippingPolicyNotFoundException exception = new ShippingPolicyNotFoundException(message);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    void testExceptionThrown() {
        String message = "Custom error message for shipping policy not found.";

        assertThatThrownBy(() -> {
            throw new ShippingPolicyNotFoundException(message);
        }).isInstanceOf(ShippingPolicyNotFoundException.class)
            .hasMessage(message);
    }
}
