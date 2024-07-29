package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.UpdateNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * NonClientOrderController : 비회원 주문 컨트롤러
 *
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

@RestController
@RequiredArgsConstructor
public class NonClientOrderControllerImpl implements NonClientOrderController {

    private final NonClientOrderService nonClientOrderService;

    // 비회원 임시 주문 저장
    @PostMapping("/api/non-client/orders/temporary")
    public ResponseEntity<String> saveNonClientTemporalOrder(@RequestHeader HttpHeaders headers,
        @RequestBody NonClientOrderForm nonClientOrderForm) {
        nonClientOrderService.saveNonClientTemporalOrder(headers, nonClientOrderForm);
        return ResponseEntity.ok().body("비회원 임시 주문 저장");
    }

    // 비회원 임시 주문 가져오기
    @GetMapping("/api/non-client/orders/temporary")
    public ResponseEntity<NonClientOrderForm> getNonClientTemporalOrder(
        @RequestHeader HttpHeaders headers, String orderCode) {
        return ResponseEntity.ok()
            .body(nonClientOrderService.getNonClientTemporalOrder(headers, orderCode));
    }

    @GetMapping("/api/non-client/orders/{orderId}")
    public ResponseEntity<NonClientOrderGetResponseDto> findNonClientOrder(
        @RequestHeader HttpHeaders headers, @PathVariable long orderId,
        @RequestParam("pwd") String orderPassword) {
        return ResponseEntity.ok()
            .body(nonClientOrderService.getOrder(headers, orderId, orderPassword));
    }

    @PostMapping("/api/non-client/orders/find-orderId")
    public ResponseEntity<List<FindNonClientOrderIdInfoResponseDto>> findNonClientOrderId(
        @RequestBody FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto,
        @RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok().body(
            nonClientOrderService.findNonClientOrderId(headers, findNonClientOrderIdRequestDto)
        );
    }

    @PutMapping("/api/non-client/orders/{orderId}/password")
    public ResponseEntity<String> updateNonClientOrderPassword(@RequestHeader HttpHeaders headers,
        @PathVariable long orderId,
        @RequestBody UpdateNonClientOrderPasswordRequestDto updateNonClientOrderPasswordRequestDto) {
        nonClientOrderService.updateNonClientOrderPassword(headers, orderId,
            updateNonClientOrderPasswordRequestDto);
        return ResponseEntity.ok("비회원 주문 비밀번호가 수정되었습니다.");
    }

}
