package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderCouponDiscountInfo;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "ClientOrderController", description = "회원 주문 관련 API")
public interface ClientOrderController {


    @Operation(
        summary = "회원 주문 임시 저장",
        description = "회원 주문 임시 저장",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "결제 승인 요청 정보"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "찾으려는 주문이 없음"
            )
        }
    )
    @PostMapping("/api/client/orders/temporary")
    ResponseEntity<String> saveClientTemporalOrder(@RequestHeader HttpHeaders headers,
        @RequestBody ClientOrderCreateForm clientOrderForm);


    @Operation(
        summary = "회원 임시 저장 주문 조회",
        description = "회원 임시 저장 주문 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 임시 저장 주문 조회 성공"
            )
        }
    )
    @GetMapping("/api/client/orders/temporary")
    ResponseEntity<ClientOrderCreateForm> getClientTemporalOrder(@RequestHeader HttpHeaders headers,
        String orderCode);


    @Operation(
        summary = "회원 주문 리스트 조회",
        description = "회원 주문 리스트 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 주문 조회 성공"
            )
        }
    )
    @GetMapping("/api/client/orders")
    ResponseEntity<Page<ClientOrderGetResponseDto>> getOrders(
        @RequestHeader HttpHeaders headers,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "sortBy", defaultValue = "orderDatetime", required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir);


    @Operation(
        summary = "회원 주문 조회",
        description = "회원 주문 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 주문 조회 성공"
            )
        }
    )
    @GetMapping("/api/client/orders/{orderId}")
    ResponseEntity<ClientOrderGetResponseDto> getOrder(@RequestHeader HttpHeaders headers,
        @PathVariable Long orderId);


    @Operation(
        summary = "회원 주문 상품 상세 리스트 조회",
        description = "회원 주문 상품 상세 리스트 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 주문 상품 상세 리스트 성공"
            )
        }
    )
    @GetMapping("/api/client/orders/{orderId}/detail")
    ResponseEntity<List<ProductOrderDetailResponseDto>> getProductOrderDetailList(
        @RequestHeader HttpHeaders headers, @PathVariable Long orderId);


    @Operation(
        summary = "회원 주문 상품 상세 단건 조회",
        description = "회원 주문 상품 상세 단건 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 주문 상품 상세 단건 조회"
            )
        }
    )
    @GetMapping("/api/client/orders/{orderId}/detail/{productOrderDetailId}")
    ResponseEntity<ProductOrderDetailResponseDto> getProductOrderDetail(
        @RequestHeader HttpHeaders headers, @PathVariable Long orderId,
        @PathVariable Long productOrderDetailId);


    @Operation(
        summary = "회원 주문 상품 옵션 상세 단건 조회",
        description = "회원 주문 상품 옵션 상세 단건 조회",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 주문 상품 옵션 상세 단건 조회"
            )
        }
    )
    @GetMapping("/api/client/orders/{orderId}/detail/{productOrderDetailId}/option")
    ResponseEntity<ProductOrderDetailOptionResponseDto> getProductOrderDetailOption(
        @RequestHeader HttpHeaders headers, @PathVariable Long orderId,
        @PathVariable Long productOrderDetailId);


    @Operation(
        summary = "회원 주문 취소 상태 변경",
        description = "회원 주문 취소 상태 변경",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 주문 취소 상태 변경"
            )
        }
    )
    @PutMapping("/api/client/orders/{orderId}/cancel")
    ResponseEntity<String> cancelOrder(@RequestHeader HttpHeaders headers,
        @PathVariable long orderId);


    @Operation(
        summary = "회원 주문 환불 상태 변경",
        description = "회원 주문 환불 상태 변경",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 주문 환불 상태 변경"
            )
        }
    )
    @PutMapping("/api/client/orders/{orderId}/refund")
    ResponseEntity<String> refundOrder(@RequestHeader HttpHeaders headers,
        @PathVariable long orderId);


    @Operation(
        summary = "회원 주문 환불 요청",
        description = "회원 주문 환불 요청",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원 주문 환불 요청"
            )
        }
    )
    @PutMapping("/api/client/orders/{orderId}/refund-request")
    ResponseEntity<String> refundRequestOrder(@RequestHeader HttpHeaders headers,
        @PathVariable long orderId);



    @Operation(
        summary = "회원이 사용가능한 쿠폰과 할인 금액 정보 얻기",
        description = "회원이 사용가능한 쿠폰과 할인 금액 정보 얻기",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "회원이 사용가능한 쿠폰과 할인 금액 정보 얻기 성공"
            )
        }
    )
    @PostMapping("/api/client/orders/coupon-info")
    ResponseEntity<List<OrderCouponDiscountInfo>> getCouponDiscountInfoList(
        @RequestHeader HttpHeaders headers,
        @RequestBody ClientOrderCreateForm clientOrderCreateForm);

}