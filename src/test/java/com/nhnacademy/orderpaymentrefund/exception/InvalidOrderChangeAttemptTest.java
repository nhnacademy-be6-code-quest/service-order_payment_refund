package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvalidOrderChangeAttemptTest {

    @Test
    void testDefaultConstructor() {
        InvalidOrderChangeAttempt exception = new InvalidOrderChangeAttempt();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("잘못된 상태변경 시도입니다.");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        InvalidOrderChangeAttempt exception = new InvalidOrderChangeAttempt(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message";
        assertThatThrownBy(() -> {
            throw new InvalidOrderChangeAttempt(customMessage);
        }).isInstanceOf(InvalidOrderChangeAttempt.class)
            .hasMessage(customMessage);
    }

    @Test
    void testDefaultMessageExceptionThrown() {
        assertThatThrownBy(() -> {
            throw new InvalidOrderChangeAttempt();
        }).isInstanceOf(InvalidOrderChangeAttempt.class)
            .hasMessage("잘못된 상태변경 시도입니다.");
    }
}
