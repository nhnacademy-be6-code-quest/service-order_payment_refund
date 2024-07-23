package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ClientOrderControllerImpl implements ClientOrderController {

    private final ClientOrderService clientOrderService;

    // 회원 임시 주문 저장
    @PostMapping("/api/client/orders/temporary")
    public ResponseEntity<String> saveClientTemporalOrder(@RequestHeader HttpHeaders headers, @RequestBody ClientOrderCreateForm clientOrderForm){
        clientOrderService.saveClientTemporalOrder(headers, clientOrderForm);
        return ResponseEntity.ok().body("주문이 임시저장 되었습니다");
    }

    // 회원 임시 주문 가져오기
    @GetMapping("/api/client/orders/temporary")
    public ResponseEntity<ClientOrderCreateForm> getClientTemporalOrder(@RequestHeader HttpHeaders headers, String tossOrderId){
        return ResponseEntity.ok().body(clientOrderService.getClientTemporalOrder(headers, tossOrderId));
    }

    // 주문 내역(Page) 조회 - 모든
    @GetMapping("/api/client/orders")
    public ResponseEntity<Page<ClientOrderGetResponseDto>> getOrders(
            @RequestHeader HttpHeaders headers,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "sortBy", defaultValue = "orderDatetime", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir){
        return ResponseEntity.ok(clientOrderService.getOrders(headers, pageSize, pageNo, sortBy, sortDir));
    }

    @GetMapping("/api/client/orders/{orderId}")
    public ResponseEntity<ClientOrderGetResponseDto> getOrder(@RequestHeader HttpHeaders headers, @PathVariable Long orderId){
        return ResponseEntity.ok(clientOrderService.getOrder(headers, orderId));
    }

    @GetMapping("/api/client/orders/{orderId}/detail")
    public ResponseEntity<List<ProductOrderDetailResponseDto>> getProductOrderDetailList(@RequestHeader HttpHeaders headers, @PathVariable Long orderId){
        return ResponseEntity.ok().body(clientOrderService.getProductOrderDetailResponseDtoList(headers, orderId));
    }

    @GetMapping("/api/client/orders/{orderId}/detail/{productOrderDetailId}")
    public ResponseEntity<ProductOrderDetailResponseDto> getProductOrderDetail(@RequestHeader HttpHeaders headers, @PathVariable Long orderId,
                                                                               @PathVariable Long productOrderDetailId){
        return ResponseEntity.ok().body(clientOrderService.getProductOrderDetailResponseDto(headers, orderId, productOrderDetailId));
    }

    @GetMapping("/api/client/orders/{orderId}/detail/{productOrderDetailId}/option")
    public ResponseEntity<ProductOrderDetailOptionResponseDto> getProductOrderDetailOption(@RequestHeader HttpHeaders headers,@PathVariable Long orderId,
                                                                                           @PathVariable Long productOrderDetailId){
        return ResponseEntity.ok().body(clientOrderService.getProductOrderDetailOptionResponseDto(headers, orderId, productOrderDetailId));
    }

    @PutMapping("/api/client/orders/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId){
        clientOrderService.cancelOrder(headers, orderId);
        return ResponseEntity.ok("주문 취소 상태로 변경되었습니다.");
    }

    @PutMapping("/api/client/orders/{orderId}/refund")
    public ResponseEntity<String> refundOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId){
        clientOrderService.refundOrder(headers, orderId);
        return ResponseEntity.ok("반품 상태로 변경되었습니다.");
    }

    @PutMapping("/api/client/orders/{orderId}/refund-request")
    public ResponseEntity<String> refundRequestOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId){
        clientOrderService.refundOrderRequest(headers, orderId);
        return ResponseEntity.ok("반품 요청 상태로 변경되었습니다.");
    }

    @GetMapping("/api/client/orders/{orderId}/order-status")
    public ResponseEntity<String> getOrderStatus(@RequestHeader HttpHeaders headers, @PathVariable long orderId){
        return ResponseEntity.ok().body(clientOrderService.getOrderStatus(headers, orderId));
    }

}
