package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}/payment-request")
    public ResponseEntity<PaymentOrderShowRequestDto> getPaymentOrderShowRequestDto(@PathVariable Long orderId){
        return ResponseEntity.ok().body(orderService.getPaymentOrderShowRequestDto(orderId));
    }

    @GetMapping("/{orderId}/approve-request")
    public ResponseEntity<PaymentOrderApproveRequestDto> getPaymentOrderApproveRequestDto(@PathVariable Long orderId){
        return ResponseEntity.ok().body(orderService.getPaymentOrderApproveRequestDto(orderId));
    }

}
