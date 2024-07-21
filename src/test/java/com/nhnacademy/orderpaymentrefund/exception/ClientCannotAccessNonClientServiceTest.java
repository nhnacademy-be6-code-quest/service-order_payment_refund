package com.nhnacademy.orderpaymentrefund.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ClientCannotAccessNonClientServiceTest {

    @Test
    void testDefaultConstructor() {
        ClientCannotAccessNonClientService exception = new ClientCannotAccessNonClientService();

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("회원은 비회원 서비스에 접근할 수 없습니다!");
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Custom error message";
        ClientCannotAccessNonClientService exception = new ClientCannotAccessNonClientService(customMessage);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    void testExceptionThrown() {
        String customMessage = "Custom error message";
        assertThatThrownBy(() -> {
            throw new ClientCannotAccessNonClientService(customMessage);
        }).isInstanceOf(ClientCannotAccessNonClientService.class)
            .hasMessage(customMessage);
    }

    @Test
    void testDefaultMessageExceptionThrown() {
        assertThatThrownBy(() -> {
            throw new ClientCannotAccessNonClientService();
        }).isInstanceOf(ClientCannotAccessNonClientService.class)
            .hasMessage("회원은 비회원 서비스에 접근할 수 없습니다!");
    }
}
