package com.nhnacademy.orderpaymentrefund.dto.payment.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class   KakaoPaymentReadyRequestDto {
    private String cid; // 가맹점 코드 ==  clientId
    @JsonProperty("partner_order_id")
    private String partnerOrderId; // 가맹점 주문번호
    @JsonProperty("partner_user_id")
    private String partnerUserId; // 가맹점 회원 아이디
    @JsonProperty("item_name")
    private String itemName; // 상품명
    private Integer quantity; // 상품수량
    @JsonProperty("total_amount")
    private Integer totalAmount; // 상품 총액
    @JsonProperty("tax_free_amount")
    private Integer taxFreeAmount; // 상품 비과세 금액
    @JsonProperty("approval_url")
    private String approvalUrl; // 결제 성공 url
    @JsonProperty("cancel_url")
    private String cancelUrl; // 결제 취소 url
    @JsonProperty("fail_url")
    private String failUrl; // 결제 실패 url
}
