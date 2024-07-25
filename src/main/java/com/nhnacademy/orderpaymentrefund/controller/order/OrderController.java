package com.nhnacademy.orderpaymentrefund.controller.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * 주문 컨트롤러
 *
 * @author 박희원
 * @version 1.0
 */


@Tag(name = "OrderController", description = "관리자 및 시스템 주문 관련 API")
public interface OrderController {


    @Operation(
        summary = "결제 승인 요청 정보 조회",
        description = "Toss - 결제 승인 요청 정보를 조회합니다.",
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
    @GetMapping("/api/order/{orderCode}/payment-request")
    ResponseEntity<PaymentOrderShowRequestDto> getPaymentOrderShowRequestDto(
        @Parameter(description = "Toss 결제 주문 ID") @PathVariable String orderCode,
        HttpServletRequest request,
        @RequestHeader HttpHeaders headers);





    @Operation(
        summary = "결제 승인 요청 정보 조회",
        description = "Toss - 결제 승인을 위한 요청 정보를 조회합니다.",
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
    @GetMapping("/api/order/{orderCode}/approve-request")
    ResponseEntity<PaymentOrderApproveRequestDto> getPaymentOrderApproveRequestDto(
        @Parameter(description = "Toss 결제 주문 ID") @PathVariable String orderCode,
        HttpServletRequest request,
        @RequestHeader HttpHeaders headers);




    @Operation(
        summary = "주문 상태 변경",
        description = "주문 상태를 변경합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "주문 상태가 변경되었습니다."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "잘못된 주문 상태 변경임. 요청한 주문상태로 변경할 수 없는 조건임."
            )
        }
    )
    @PutMapping("/api/order/{orderId}")
    ResponseEntity<String> updateOrderStatus(
        @Parameter(description = "주문 ID") @PathVariable(name = "orderId") Long orderId,
        @Parameter(description = "변경할 주문 상태") @RequestParam(name = "status") String status);





    @Operation(
        summary = "전체 주문 조회",
        description = "페이지네이션을 통해 전체 주문 목록을 조회합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "주문 목록"
            )
        }
    )
    @GetMapping("/api/order/all")
    ResponseEntity<Page<OrderResponseDto>> getOrder(
        @Parameter(description = "페이지 사이즈", example = "5") @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
        @Parameter(description = "페이지 번호", example = "0") @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @Parameter(description = "정렬 기준", example = "orderDatetime") @RequestParam(value = "sortBy", defaultValue = "orderDatetime", required = false) String sortBy,
        @Parameter(description = "정렬 방향", example = "desc") @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir);




    @Operation(
        summary = "주문 상세 조회",
        description = "특정 주문의 상세 정보를 조회합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "주문 상세 정보"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "찾으려는 주문이 없음"
            )
        }
    )
    @GetMapping("/api/order/{orderId}/detail")
    ResponseEntity<List<ProductOrderDetailResponseDto>> getProductOrderDetailList(
        @Parameter(description = "주문 ID") @PathVariable Long orderId);




    @Operation(
        summary = "주문 상세 항목 조회",
        description = "특정 주문의 특정 상품 상세 정보를 조회합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "상품 상세 정보"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "찾으려는 주문이 없음"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "찾으려는 주문과 연관된 주문상품상세가 없음"
            )
        }
    )
    @GetMapping("/api/order/{orderId}/detail/{productOrderDetailId}")
    ResponseEntity<ProductOrderDetailResponseDto> getProductOrderDetail(
        @Parameter(description = "주문 ID") @PathVariable Long orderId,
        @Parameter(description = "상품 주문 상세 ID") @PathVariable Long productOrderDetailId);




    @Operation(
        summary = "주문 상세 옵션 정보 조회",
        description = "특정 주문의 특정 상품 옵션 상세 정보를 조회합니다.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "상품 옵션 상세 정보"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "찾으려는 주문상품옵션이 없음"
            )

        }
    )
    @GetMapping("/api/order/{orderId}/detail/{productOrderDetailId}/option")
    ResponseEntity<ProductOrderDetailOptionResponseDto> getProductOrderDetailOption(
        @Parameter(description = "주문 ID") @PathVariable Long orderId,
        @Parameter(description = "상품 주문 상세 ID") @PathVariable Long productOrderDetailId);
}
