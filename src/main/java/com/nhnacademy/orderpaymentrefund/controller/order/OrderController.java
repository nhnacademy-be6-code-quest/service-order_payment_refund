package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/order-status/{orderDetailId}")
    public ResponseEntity<String> getOrderStatus(@PathVariable long orderDetailId) {
        Optional<OrderStatus> orderStatus = orderService.getOrderStatusByOrderDetailId(orderDetailId);
        return orderStatus
            .map(status -> ResponseEntity.ok(status.toString()))
            .orElseGet(() -> ResponseEntity.ok("Order status not found for orderDetailId " + orderDetailId));
    }

}
