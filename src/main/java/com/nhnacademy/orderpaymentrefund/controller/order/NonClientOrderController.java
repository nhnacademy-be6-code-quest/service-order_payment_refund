package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/non-client/orders")
@AllArgsConstructor
public class NonClientOrderController {

    private NonClientOrderService nonClientOrderService;

    @PostMapping
    public ResponseEntity<String> createNonClientOrder(@RequestBody CreateNonClientOrderRequestDto createNonClientOrderRequestDto){
        return ResponseEntity.created(nonClientOrderService.tryCreateOrder(createNonClientOrderRequestDto)).body("주문이 완료되었습니다");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FindNonClientOrderDetailResponseDto> findOrderDetail(@PathVariable(name = "orderId") long orderId, @RequestParam String orderPassword){
        return ResponseEntity.ok().body(nonClientOrderService.findNonClientOrderDetail(orderId, orderPassword));
    }

    @GetMapping("/find-orderId")
    public ResponseEntity<Long> findNonClientOrderId(@ModelAttribute FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto){
        return ResponseEntity.ok().body(nonClientOrderService.findNonClientOrderId(findNonClientOrderIdRequestDto));
    }

    @GetMapping("/find-password")
    public ResponseEntity<String> findNonClientOrderPassword(@ModelAttribute FindNonClientOrderPasswordRequestDto findNonClientOrderPasswordRequestDto){
        return ResponseEntity.ok().body(nonClientOrderService.findNonClientOrderPassword(findNonClientOrderPasswordRequestDto));
    }
}
