package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.UpdateNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "NonClientOrderController", description = "비회원 주문 관련 API")
public interface NonClientOrderController {


    @Operation(
        summary = "비회원 주문 임시 저장",
        description = "비회원 주문 임시 저장",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "주문 임시 저장 성공"
            )
        }
    )
    @PostMapping("/api/non-client/orders/temporary")
    ResponseEntity<String> saveNonClientTemporalOrder(@RequestHeader HttpHeaders headers,
        @RequestBody NonClientOrderForm nonClientOrderForm);




    @Operation(
        summary = "비회원 임시 주문 조회",
        description = "비회원 임시 주문 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "임시 주문 조회 성공"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "임시 주문 조회 실패. 회원이 비회원 주문 조회를 시도하고 있음. 잘못된 접근."
            )
        }
    )
    @GetMapping("/api/non-client/orders/temporary")
    ResponseEntity<NonClientOrderForm> getNonClientTemporalOrder(
        @RequestHeader HttpHeaders headers, String orderCode);




    @Operation(
        summary = "비회원 주문 조회",
        description = "비회원 주문 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "비회원 주문 조회에 성공"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "비회원 주문 조회 실패. 주문을 찾을 수 없음"
            )
        }
    )
    @GetMapping("/api/non-client/orders/{orderId}")
    ResponseEntity<NonClientOrderGetResponseDto> findNonClientOrder(
        @RequestHeader HttpHeaders headers, @PathVariable long orderId,
        @RequestParam("pwd") String orderPassword);




    @Operation(
        summary = "비회원 주문 번호 찾기",
        description = "비회원 주문 번호 찾기",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "해당 비회원이 주문했던 과거 주문 이력들을 조회하는데 성공."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "주문을 찾을 수 없음"
            )
        }
    )
    @PostMapping("/api/non-client/orders/find-orderId")
    ResponseEntity<List<FindNonClientOrderIdInfoResponseDto>> findNonClientOrderId(
        @RequestBody FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto,
        @RequestHeader HttpHeaders headers);




    @Operation(
        summary = "비회원 주문 번호 수정",
        description = "비회원 주문 번호 수정",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "비회원의 주문 비밀번호 수정 성공"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "주문을 찾을 수 없음"
            )
        }
    )
    @PutMapping("/api/non-client/orders/{orderId}/password")
    ResponseEntity<String> updateNonClientOrderPassword(@RequestHeader HttpHeaders headers,
        @PathVariable long orderId,
        @RequestBody UpdateNonClientOrderPasswordRequestDto updateNonClientOrderPasswordRequestDto);



}
