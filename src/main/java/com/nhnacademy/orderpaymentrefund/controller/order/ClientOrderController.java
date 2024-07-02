package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/api/client/orders")
@RequiredArgsConstructor
public class ClientOrderController {

    private final ClientOrderService clientOrderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestHeader HttpHeaders headers, @RequestBody CreateClientOrderRequestDto createClientOrderRequestDto){
        return ResponseEntity.created(clientOrderService.tryCreateOrder(headers, createClientOrderRequestDto)).body("주문이 완료되었습니다");
    }

    @GetMapping
    public ResponseEntity<Page<FindClientOrderResponseDto>> findClientOrderList(@RequestHeader HttpHeaders headers, Pageable pageable){
        return ResponseEntity.ok(clientOrderService.findClientOrderList(headers, pageable));
    }

}
