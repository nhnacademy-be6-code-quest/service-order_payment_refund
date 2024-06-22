package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Virtus_Chae
 * @version 0.0
 */
@RestController
public class RefundController {
    /**
     * 사용자에게 환불 창을 보여 줍니다. 미완성입니다.
     */
    @GetMapping("client/order/{orderId}/refund")
    public String refund(@PathVariable String orderId, Model model) {
        return "refund";
    }

    /**
     * 환불을 진행합니다. 미완성입니다.
     */
    @PostMapping("client/order/{orderId}/refund")
    public String refund(@PathVariable String orderId, @ModelAttribute("payment") Payment payment) {
        return "refund";
    }
}