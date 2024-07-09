package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 결제 정보를 저장하는 컨트롤러 인터페이스입니다. 결제 정보는 수정되거나 삭제되지 않습니다.
 *
 * @author 김채호
 * @version 1.0
 */
public interface PaymentControllerJavaDoc {

    /**
     * 결제 정보를 저장합니다.
     *
     * @param orderId                 주문 ID
     * @param tossPaymentsResponseDto Toss 결제 응답 DTO
     */
    @PostMapping("/api/client/order/{orderId}/payment")
    void savePayment(@PathVariable long orderId,
        @RequestBody TossPaymentsResponseDto tossPaymentsResponseDto);
}
