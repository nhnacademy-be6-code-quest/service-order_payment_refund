package com.nhnacademy.orderpaymentrefund.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FailToPreProcessingTest {

    @Test
    void testDefaultConstructor() {
        FailToPreProcessing exception = new FailToPreProcessing();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("주문 및 결제 전처리 실패");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        FailToPreProcessing exception = new FailToPreProcessing(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message";
        assertThatThrownBy(() -> {
            throw new FailToPreProcessing(customMessage);
        }).isInstanceOf(FailToPreProcessing.class)
            .hasMessage(customMessage);
    }

    @Test
    void testDefaultMessageExceptionThrown() {
        assertThatThrownBy(() -> {
            throw new FailToPreProcessing();
        }).isInstanceOf(FailToPreProcessing.class)
            .hasMessage("주문 및 결제 전처리 실패");
    }
}
