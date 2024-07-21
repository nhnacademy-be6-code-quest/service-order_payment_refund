package com.nhnacademy.orderpaymentrefund.config;

import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final URI clientOrderURI = URI.create("/api/client/orders/**");
    private static final URI commonOrderURI = URI.create("/api/orders/**");

    @Bean
    @SuppressWarnings("java:S4502") // Be sure to disable csrf
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .authorizeHttpRequests(req -> req.anyRequest().permitAll())
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new HeaderFilter(List.of(
                // 회원이나 롤 있는거만 헤더 걸기.
                new HeaderFilter.RouteConfig(clientOrderURI, HttpMethod.GET.name(),
                    Collections.emptyList()),
                new HeaderFilter.RouteConfig(clientOrderURI, HttpMethod.POST.name(),
                    Collections.emptyList()),
                new HeaderFilter.RouteConfig(clientOrderURI, HttpMethod.PUT.name(),
                    Collections.emptyList()),
                new HeaderFilter.RouteConfig(clientOrderURI, HttpMethod.DELETE.name(),
                    Collections.emptyList()),
                new HeaderFilter.RouteConfig(commonOrderURI, HttpMethod.GET.name(),
                    Collections.emptyList()),
                new HeaderFilter.RouteConfig(commonOrderURI, HttpMethod.POST.name(),
                    Collections.emptyList()),
                new HeaderFilter.RouteConfig(commonOrderURI, HttpMethod.PUT.name(),
                    Collections.emptyList()),
                new HeaderFilter.RouteConfig(commonOrderURI, HttpMethod.DELETE.name(),
                    Collections.emptyList())
            )
            ), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}