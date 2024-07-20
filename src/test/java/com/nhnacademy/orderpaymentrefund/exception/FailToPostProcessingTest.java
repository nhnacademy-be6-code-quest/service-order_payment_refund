package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FailToPostProcessingTest {

    @Test
    void testDefaultConstructor() {
        FailToPostProcessing exception = new FailToPostProcessing();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("주문 및 결제 후처리에 실패하였습니다");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        FailToPostProcessing exception = new FailToPostProcessing(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message";
        assertThatThrownBy(() -> {
            throw new FailToPostProcessing(customMessage);
        }).isInstanceOf(FailToPostProcessing.class)
            .hasMessage(customMessage);
    }

    @Test
    void testDefaultMessageExceptionThrown() {
        assertThatThrownBy(() -> {
            throw new FailToPostProcessing();
        }).isInstanceOf(FailToPostProcessing.class)
            .hasMessage("주문 및 결제 후처리에 실패하였습니다");
    }
}
