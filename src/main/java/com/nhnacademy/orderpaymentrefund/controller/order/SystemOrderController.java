package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.system.SystemOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.system.SystemOrderPutRequestDto;
import com.nhnacademy.orderpaymentrefund.service.order.SystemOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/order")
@AllArgsConstructor
public class SystemOrderController { // 시스템은 생성, 수정만 가능

    private SystemOrderService systemOrderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody SystemOrderPostRequestDto systemOrderPostRequestDto){
        systemOrderService.createOrder(systemOrderPostRequestDto);
        return ResponseEntity.ok("Order가 생성되었습니다");
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> changeOrder(@PathVariable long orderId, @RequestBody SystemOrderPutRequestDto systemOrderPutRequestDto){
        systemOrderService.updateOrder(orderId, systemOrderPutRequestDto);
        return ResponseEntity.ok("Order가 수정되었습니다");
    }

}
