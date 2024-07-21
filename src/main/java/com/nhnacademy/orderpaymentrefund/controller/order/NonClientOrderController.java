package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * NonClientOrderController : 비회원 주문 컨트롤러
 * @author 박희원(bakhuiwon326)
 * @version 2.0
 **/

@RestController
@RequiredArgsConstructor
public class NonClientOrderController {

    private final NonClientOrderService nonClientOrderService;

    // 비회원 임시 주문 저장
    @PostMapping("/api/non-client/orders/temporary")
    public ResponseEntity<String> saveNonClientTemporalOrder(@RequestHeader HttpHeaders headers, @RequestBody NonClientOrderForm nonClientOrderForm){
        nonClientOrderService.saveNonClientTemporalOrder(headers, nonClientOrderForm);
        return ResponseEntity.ok().body("비회원 임시 주문 저장");
    }

    // 비회원 임시 주문 가져오기
    @GetMapping("/api/non-client/orders/temporary")
    public ResponseEntity<NonClientOrderForm> getNonClientTemporalOrder(@RequestHeader HttpHeaders headers, String tossOrderId){
        return ResponseEntity.ok().body(nonClientOrderService.getNonClientTemporalOrder(headers, tossOrderId));
    }

    @GetMapping("/api/non-client/orders/{orderId}")
    public ResponseEntity<NonClientOrderGetResponseDto> findNonClientOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId, @RequestParam("pwd") String orderPassword){
        return ResponseEntity.ok().body(nonClientOrderService.getOrder(headers, orderId, orderPassword));
    }

    @GetMapping("/api/non-client/orders/find-orderId")
    public ResponseEntity<Page<FindNonClientOrderIdInfoResponseDto>> findNonClientOrderId(@ModelAttribute FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto,
                                                                                          @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
                                                                                          @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                                          @RequestParam(value = "sortBy", defaultValue = "orderDatetime", required = false) String sortBy,
                                                                                          @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir,
                                                                                          @RequestHeader HttpHeaders headers){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return ResponseEntity.ok().body(nonClientOrderService.findNonClientOrderId(headers, findNonClientOrderIdRequestDto, PageRequest.of(pageNo, pageSize, sort)));
    }

    @GetMapping("/api/non-client/orders/find-password")
    public ResponseEntity<String> findNonClientOrderPassword(@RequestHeader HttpHeaders headers,
                                                             FindNonClientOrderPasswordRequestDto findNonClientOrderPasswordRequestDto){
        return ResponseEntity.ok().body(nonClientOrderService.findNonClientOrderPassword(headers, findNonClientOrderPasswordRequestDto));
    }

    @PutMapping("/api/non-client/orders/{orderId}/payment-complete")
    public ResponseEntity<String> paymentCompleteOrder(@RequestHeader HttpHeaders headers, @PathVariable long orderId){
        nonClientOrderService.paymentCompleteOrder(headers, orderId);
        return ResponseEntity.ok("주문 결제완료 되었습니다.");
    }

}
