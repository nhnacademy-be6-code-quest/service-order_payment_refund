package com.nhnacademy.payment.controller;

import com.nhnacademy.payment.domain.Payment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @author Virtus_Chae
 * @version 0.0
 */
@Controller
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