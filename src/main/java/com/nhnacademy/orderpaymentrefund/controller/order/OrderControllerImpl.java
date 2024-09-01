package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    @GetMapping("/api/order/{orderCode}/payment-view-request")
    public ResponseEntity<PaymentViewRequestDto> getPaymentViewRequestDto(@PathVariable String orderCode, @RequestParam String pgName){
        return ResponseEntity.ok().body(orderService.getPaymentViewRequestDto(pgName, orderCode));
    }

    @PutMapping("/api/order/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable(name = "orderId") Long orderId, @RequestParam(name = "status") String status){
        orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.ok().body("주문 상태가 변경되었습니다.");
    }

    @GetMapping("/api/order/all")
    public ResponseEntity<Page<OrderResponseDto>> getOrder(@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                           @RequestParam(value = "sortBy", defaultValue = "orderDatetime", required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir){
        return ResponseEntity.ok().body(orderService.getAllOrderList(pageSize, pageNo, sortBy, sortDir));
    }

    @GetMapping("/api/order/{orderId}/detail")
    public ResponseEntity<List<ProductOrderDetailResponseDto>> getProductOrderDetailList(@PathVariable Long orderId){
        return ResponseEntity.ok().body(orderService.getProductOrderDetailList(orderId));
    }

    @GetMapping("/api/order/{orderId}/detail/{productOrderDetailId}")
    public ResponseEntity<ProductOrderDetailResponseDto> getProductOrderDetail(@PathVariable Long orderId, @PathVariable Long productOrderDetailId){
        return ResponseEntity.ok().body(orderService.getProductOrderDetail(orderId, productOrderDetailId));
    }

    @GetMapping("/api/order/{orderId}/detail/{productOrderDetailId}/option")
    public ResponseEntity<ProductOrderDetailOptionResponseDto> getProductOrderDetailOption(@PathVariable Long orderId, @PathVariable Long productOrderDetailId){
        return ResponseEntity.ok().body(orderService.getProductOrderDetailOptionResponseDto(orderId, productOrderDetailId));
    }

}
