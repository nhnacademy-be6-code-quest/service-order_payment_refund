package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    @GetMapping("/api/order/{tossOrderId}/payment-request")
    public ResponseEntity<PaymentOrderShowRequestDto> getPaymentOrderShowRequestDto(@PathVariable String tossOrderId, HttpServletRequest request, @RequestHeader HttpHeaders headers){
        return ResponseEntity.ok().body(orderService.getPaymentOrderShowRequestDto(headers, request, tossOrderId));
    }

    @GetMapping("/api/order/{tossOrderId}/approve-request")
    public ResponseEntity<PaymentOrderApproveRequestDto> getPaymentOrderApproveRequestDto(@PathVariable String tossOrderId, HttpServletRequest request, @RequestHeader HttpHeaders headers){
        return ResponseEntity.ok().body(orderService.getPaymentOrderApproveRequestDto(headers, request, tossOrderId));
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
