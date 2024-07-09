package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestHeader HttpHeaders headers, @RequestBody ClientOrderFormRequestDto clientOrderForm){
        return ResponseEntity.created(null).body(clientOrderService.tryCreateOrder(headers, clientOrderForm));
    }

    @GetMapping
    public ResponseEntity<Page<FindClientOrderResponseDto>> findClientOrderList(@RequestHeader HttpHeaders headers, Pageable pageable){
        return ResponseEntity.ok(clientOrderService.findClientOrderList(headers, pageable));
    }

}
