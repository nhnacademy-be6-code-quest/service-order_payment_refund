package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class KakaoPaymentReadyResponseDto {
    private String tid;
    @JsonProperty("next_redirect_app_url")
    private String nextRedirectAppUrl;
    @JsonProperty("next_redirect_mobile_url")
    private String nextRedirectMobilUrl;
    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;
    @JsonProperty("android_app_scheme")
    private String androidAppScheme;
    @JsonProperty("ios_app_scheme")
    private String iosAppScheme;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
