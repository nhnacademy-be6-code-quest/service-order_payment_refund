package com.nhnacademy.orderpaymentrefund.config;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest
@Import({SecurityConfigTest.TestConfig.class, SecurityConfig.class})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AuthenticationConfiguration authenticationConfiguration;

    @MockBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.when(authenticationConfiguration.getAuthenticationManager())
            .thenReturn(authenticationManager);
    }

    @Test
    @WithMockUser
    void testSecurityFilterChain() throws Exception {
        mockMvc.perform(get("/anyEndpoint"))
            .andExpect(status().isOk());
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/anyEndpoint"))
            .andExpect(status().isOk());
    }

    @Configuration
    static class TestConfig {

        @RestController
        static class MockController {

            @GetMapping("/anyEndpoint")
            public String anyEndpoint() {
                return "Success";
            }
        }
    }
}