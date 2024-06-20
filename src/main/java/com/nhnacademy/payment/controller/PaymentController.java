package com.nhnacademy.payment.controller;

import com.nhnacademy.payment.domain.Payment;
import com.nhnacademy.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Virtus_Chae
 * @version 0.0
 */
@RestController
public class PaymentController {
    PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * 사용자가 결제 창에 들어왔을 때 보유 중인 포인트와 쿠폰 리스트를 보여줍니다. 미완성입니다.
     */
    @GetMapping("/client/order/payment")
    public String createPayment(Model model) {
        return "payment";
    }

    /**
     * 사용자가 결제 창에 들어왔을 때 입력한 값을 POST 로 보냅니다. 미완성입니다.
     */
    @PostMapping("/client/order/payment")
    public String createPayment(@ModelAttribute("payment") Payment payment) {
        return "payment";
    }
}