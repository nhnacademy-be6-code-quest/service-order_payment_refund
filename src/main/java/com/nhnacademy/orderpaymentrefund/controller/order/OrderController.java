package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<PaymentOrderShowRequestDto> getPaymentOrderShowRequestDto(@RequestHeader HttpHeaders headers, @PathVariable Long orderId){
        return ResponseEntity.ok().body(orderService.getPaymentOrderShowRequestDto(headers, orderId));
    }

    @GetMapping("/{orderId}/approve-request")
    public ResponseEntity<PaymentOrderApproveRequestDto> getPaymentOrderApproveRequestDto(@RequestHeader HttpHeaders headers, HttpServletRequest req, @PathVariable Long orderId){
        return ResponseEntity.ok().body(orderService.getPaymentOrderApproveRequestDto(headers, orderId));
    }

}
