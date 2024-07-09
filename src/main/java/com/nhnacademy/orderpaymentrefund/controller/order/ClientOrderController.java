package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderListGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestHeader HttpHeaders headers, @RequestBody ClientOrderFormRequestDto clientOrderForm){
        return ResponseEntity.created(null).body(clientOrderService.tryCreateOrder(headers, clientOrderForm));
    }

    // 주문 내역(Page) 조회 - 모든
    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getOrders(
            @RequestHeader HttpHeaders headers,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "sortBy", defaultValue = "orderDate", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir){
        return ResponseEntity.ok(clientOrderService.getOrders(headers, pageSize, pageNo, sortBy, sortDir));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId){
        return ResponseEntity.ok(clientOrderService.getOrder(headers, orderId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId){
        clientOrderService.cancelOrder(headers, orderId);
        return ResponseEntity.ok("주문 취소 되었습니다.");
    }

    @PutMapping("/{orderId}/refund")
    public ResponseEntity<String> refundOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId){
        clientOrderService.refundOrder(headers, orderId);
        return ResponseEntity.ok("환불처리 되었습니다.");
    }

}
