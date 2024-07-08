package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 환불 정보를 저장하고, 조회하는 컨트롤러 인터페이스입니다. 환불 정보는 수정되거나 삭제되지 않습니다.
 */
public interface RefundControllerJavaDoc {

    /**
     * 사용자에게 환불 창을 보여 줍니다. 미완성입니다.
     */
    @GetMapping("client/order/{orderId}/refund")
    String refund(@PathVariable String orderId, Model model);

    /**
     * 환불을 진행합니다. 미완성입니다.
     */
    @PostMapping("client/order/{orderId}/refund")
    String refund(@PathVariable String orderId, @ModelAttribute("payment") Payment payment);
}
