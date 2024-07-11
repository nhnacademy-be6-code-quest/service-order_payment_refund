package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Page<OrderResponseDto>> getOrder(@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                           @RequestParam(value = "sortBy", defaultValue = "orderDatetime", required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir){
        return ResponseEntity.ok().body(orderService.getAllOrderList(pageSize, pageNo, sortBy, sortDir));
    }

    @GetMapping("/{orderId}/detail")
    public ResponseEntity<List<ProductOrderDetailResponseDto>> getProductOrderDetailList(@PathVariable Long orderId){
        return ResponseEntity.ok().body(orderService.getProductOrderDetailList(orderId));
    }

    @GetMapping("/{orderId}/detail/{productOrderDetailId}")
    public ResponseEntity<ProductOrderDetailResponseDto> getProductOrderDetail(@PathVariable Long orderId, @PathVariable Long productOrderDetailId){
        return ResponseEntity.ok().body(orderService.getProductOrderDetail(orderId, productOrderDetailId));
    }

    @GetMapping("/{orderId}/detail/{productOrderDetailId}/option")
    public ResponseEntity<ProductOrderDetailOptionResponseDto> getProductOrderDetailOption(@PathVariable Long orderId, @PathVariable Long productOrderDetailId){
        return ResponseEntity.ok().body(orderService.getProductOrderDetailOptionResponseDto(orderId, productOrderDetailId));
    }

}
