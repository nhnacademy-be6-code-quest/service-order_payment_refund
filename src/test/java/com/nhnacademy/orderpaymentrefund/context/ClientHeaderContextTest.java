package com.nhnacademy.orderpaymentrefund.context;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ClientHeaderContextTest {

    private static final String X_USER_ID = "X-User-ID";
    private static final Long TEST_USER_ID = 12345L;

    private ClientHeaderContext clientHeaderContext;

    @BeforeEach
    public void setUp() {
        clientHeaderContext = new ClientHeaderContext();
    }

    @Test
    void testSetClientId_withValidHeader() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(X_USER_ID, TEST_USER_ID.toString());

        // When
        clientHeaderContext.setClientId(request);

        // Then
        assertThat(clientHeaderContext.isClient()).isTrue();
        assertThat(clientHeaderContext.getClientId()).isEqualTo(TEST_USER_ID);

        // Clear the ThreadLocal after the test
        clientHeaderContext.clear();
    }

    @Test
    void testSetClientId_withMissingHeader() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();

        // When
        clientHeaderContext.setClientId(request);

        // Then
        assertThat(clientHeaderContext.isClient()).isFalse();
        assertThat(clientHeaderContext.getClientId()).isNull();
    }

    @Test
    void testClear() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(X_USER_ID, TEST_USER_ID.toString());
        clientHeaderContext.setClientId(request);

        // Ensure the client ID is set
        assertThat(clientHeaderContext.isClient()).isTrue();

        // When
        clientHeaderContext.clear();

        // Then
        assertThat(clientHeaderContext.isClient()).isFalse();
        assertThat(clientHeaderContext.getClientId()).isNull();
    }

}
