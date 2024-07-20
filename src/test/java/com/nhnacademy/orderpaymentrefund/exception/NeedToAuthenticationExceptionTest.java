package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NeedToAuthenticationExceptionTest {

    @Test
    void testDefaultConstructor() {
        NeedToAuthenticationException exception = new NeedToAuthenticationException();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("인증이 필요합니다");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        NeedToAuthenticationException exception = new NeedToAuthenticationException(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message";
        assertThatThrownBy(() -> {
            throw new NeedToAuthenticationException(customMessage);
        }).isInstanceOf(NeedToAuthenticationException.class)
            .hasMessage(customMessage);
    }

    @Test
    void testDefaultMessageExceptionThrown() {
        assertThatThrownBy(() -> {
            throw new NeedToAuthenticationException();
        }).isInstanceOf(NeedToAuthenticationException.class)
            .hasMessage("인증이 필요합니다");
    }
}
