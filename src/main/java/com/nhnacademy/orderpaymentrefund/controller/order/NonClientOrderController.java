package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * NonClientOrderController : 비회원 주문 컨트롤러
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

@RestController
@RequestMapping("/api/non-client/orders")
@RequiredArgsConstructor
public class NonClientOrderController {

    private final NonClientOrderService nonClientOrderService;

    @PostMapping
    public ResponseEntity<String> createNonClientOrder(@RequestBody CreateNonClientOrderRequestDto createNonClientOrderRequestDto){
        nonClientOrderService.tryCreateOrder(createNonClientOrderRequestDto);
        return ResponseEntity.created(null).body("주문이 완료되었습니다");
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<FindNonClientOrderResponseDto> findNonClientOrder(@PathVariable long orderId, @RequestParam("orderPassword") String orderPassword){
        return ResponseEntity.ok().body(nonClientOrderService.findNonClientOrder(orderId, orderPassword));
    }

    @GetMapping("/find-orderId")
    public ResponseEntity<Page<FindNonClientOrderIdInfoResponseDto>> findNonClientOrderId(@ModelAttribute FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, Pageable pageable){
        return ResponseEntity.ok().body(nonClientOrderService.findNonClientOrderId(findNonClientOrderIdRequestDto, pageable));
    }

    @GetMapping("/find-password")
    public ResponseEntity<String> findNonClientOrderPassword(@ModelAttribute FindNonClientOrderPasswordRequestDto findNonClientOrderPasswordRequestDto){
        return ResponseEntity.ok().body(nonClientOrderService.findNonClientOrderPassword(findNonClientOrderPasswordRequestDto));
    }
}
