package com.nhnacademy.orderpaymentrefund.dto.order.request.toss;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * 토스 페이먼츠에 결제와 관련된 정보를 넘기기 위한 정보들입니다. 사용자가 주문에서 '결제하기' 버튼을 눌렀을 때 결제로 넘어 오는 값입니다. 1) 쿠폰, 포인트 같은 할인
 * 금액을 제외한 순수 결제 금액. 2) 토스에 제공해야 하는 tossOrderId. 3) 주문 이름을 만들기 위한 정보들이 넘어 옵니다.
 * <p>
 * PaymentOrderValidationRequestDto 와는 다른 페이지에서 불러 오는 값입니다. 같은 Dto 를 써도 되는지 고민해 보면 좋을 것 같습니다.
 *
 * @author 박희원
 * @version 1.0
 */
@Builder
@Getter
public class PaymentOrderShowRequestDto {

    // payAmount 를 계산하기 위해 필요한 값들 : 결제 DB에 들어가야 하는 민감한 정보는 primitive type 으로 선언했습니다.
    long orderTotalAmount;          // 주문 총 금액
    long discountAmountByCoupon;    // 쿠폰으로 할인 받은 값
    long discountAmountByPoint;     // 포인트로 할인 받은 값

    // toss 에 제공해야 하는 orderId 를 제공하기 위해 필요한 값
    @NotNull
    String tossOrderId;

    // orderName : ex) "초코파이 외 10건" 을 만들어 주기 위해 필요한 정보
    @NotBlank
    String orderHistoryTitle;
}
