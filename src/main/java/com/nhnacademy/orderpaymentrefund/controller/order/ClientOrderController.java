package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.List;

@RestController
@RequestMapping("/api/client/orders")
@AllArgsConstructor
public class ClientOrderController {

    private ClientOrderService clientOrderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestHeader HttpHeaders headers, @RequestBody CreateClientOrderRequestDto createClientOrderRequestDto){
        return ResponseEntity.created(clientOrderService.tryCreateOrder(headers, createClientOrderRequestDto)).body("주문이 완료되었습니다");
    }

    @GetMapping
    public ResponseEntity<List<FindClientOrderResponseDto>> findClientOrderList(@RequestHeader HttpHeaders headers){
        return ResponseEntity.ok(clientOrderService.findClientOrderList(headers));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FindClientOrderDetailResponseDto> findClientOrderDetail(@RequestHeader HttpHeaders headers, @PathVariable(name = "orderId") long orderId){
        return ResponseEntity.ok(clientOrderService.findClientOrderDetail(orderId));
    }

}
