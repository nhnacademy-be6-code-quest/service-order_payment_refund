package com.nhnacademy.orderpaymentrefund.config;

import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final URI clientOrderURI = URI.create("/api/client/orders/**");
    private static final URI nonClientOrderURI = URI.create("/api/non-client/orders/**");
    private static final URI commonOrderURI = URI.create("/api/orders/**");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(new HeaderFilter(List.of(
//                new HeaderFilter.RouteConfig(clientOrderURI, HttpMethod.GET.name(), List.of()),
//                new HeaderFilter.RouteConfig(clientOrderURI, HttpMethod.POST.name(), List.of()),
//                new HeaderFilter.RouteConfig(clientOrderURI, HttpMethod.PUT.name(), List.of()),
//                new HeaderFilter.RouteConfig(clientOrderURI, HttpMethod.DELETE.name(), List.of()),
//                new HeaderFilter.RouteConfig(nonClientOrderURI, HttpMethod.GET.name(), List.of()),
//                new HeaderFilter.RouteConfig(nonClientOrderURI, HttpMethod.POST.name(), List.of()),
//                new HeaderFilter.RouteConfig(nonClientOrderURI, HttpMethod.PUT.name(), List.of()),
//                new HeaderFilter.RouteConfig(nonClientOrderURI, HttpMethod.DELETE.name(), List.of()),
//                new HeaderFilter.RouteConfig(commonOrderURI, HttpMethod.GET.name(), List.of()),
//                new HeaderFilter.RouteConfig(commonOrderURI, HttpMethod.POST.name(), List.of()),
//                new HeaderFilter.RouteConfig(commonOrderURI, HttpMethod.PUT.name(), List.of()),
                new HeaderFilter.RouteConfig(commonOrderURI, HttpMethod.DELETE.name(), List.of())
            )), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(req ->
                req.anyRequest().permitAll()
            )
            .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}