package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable(name = "orderId") Long orderId, @RequestParam(name = "status") String status){
        orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.ok().body("주문 상태가 변경되었습니다.");
    }

    @GetMapping("/all")
    public ResponseEntity<Page<OrderResponseDto>> getOrder(@PageableDefault(size = 10, page = 0, sort = "orderDatetime", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok().body(orderService.getAllOrderList(pageable));
    }

}
