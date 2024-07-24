package com.nhnacademy.orderpaymentrefund.dto.payment.response;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 결제가 성공했을 때 사용자에게 적절한 정보를 보여줄 때, 그리고 DB 에 저장될 때 사용되는 DTO 입니다. 토스 페이먼츠에서 넘어 오는 값들을 파싱하여 만듭니다. 이 정보가
 * 보이면 이미 결제는 승인까지 마치고, 고객의 계좌에서 돈은 나간 상태입니다.
 *
 * @author 김채호
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TossPaymentsResponseDto {

    @NotBlank
    String orderName;

    long totalAmount;

    @NotBlank
    String method;              // ex) "카드", "가상게좌", "계좌이체", "휴대폰"

    @NotBlank
    String paymentKey;

    @Setter
    @Nullable                   // 결제 수단에 따라 Null 이 될 수 있음. 이해 돕기 위한 애너테이션 사용.
    String cardNumber;          // method 가 카드일 때 : 카드 번호

    @Setter
    @Nullable
    String accountNumber;       // method 가 가상계좌일 때 : 발급된 계좌번호

    @Setter
    @Nullable
    String bank;                // method 가 계좌이체일 때 : 은행 숫자 코드 (View 에서 처리 예정)

    @Setter
    @Nullable
    String customerMobilePhone; // method 가 휴대폰일 때 : 구매자가 결제에 사용한 휴대폰 번호

    String orderId; // orderCode
}