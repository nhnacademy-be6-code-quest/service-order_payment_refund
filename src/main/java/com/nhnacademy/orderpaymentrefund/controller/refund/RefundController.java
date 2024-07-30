package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundAfterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRegisterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyListResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundSuccessResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Refund", description = "환불 관련 API")
public interface RefundController {

    @Operation(
        summary = "주문 취소",
        description = "Order - 사용자 주문취소",
        responses = {
            @ApiResponse(
                responseCode = "200"
            )
        }
    )
    @PostMapping("/api/refund/cancel")
    void paymentCancel(
        @Parameter(description = "주문취소에 필요한 정보")
        @RequestBody PaymentCancelRequestDto paymentCancelRequestDto);


    @Operation(
        summary = "상품 반품",
        description = "Refund - 사용자 반품요청",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "환불정책정보"
            )
        }
    )
    @GetMapping("/api/refund/request")
    ResponseEntity<List<RefundPolicyResponseDto>> refundRequest(
        @Parameter(description = "주문아이디")
        @RequestParam long orderId);

    @Operation(
        summary = "상품 반품",
        description = "Refund - 사용자 반품요청",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "환불금액"
            )
        }
    )
    @PostMapping("/api/refund/request")
    RefundSuccessResponseDto refundRequest(
        @Parameter(description = "반품 저장을 위한 정보")
        @RequestBody RefundRequestDto requestDto);

    @Operation(
        summary = "상품 반품",
        description = "Refund - 사용자 반품요청수락",
        responses = {
            @ApiResponse(
                responseCode = "200"
            )
        }
    )
    @PostMapping("/api/refund/admin/refund")
    void refundAccess(
        @Parameter(description = "반품요청 수락에 필요한 정보")
        @RequestBody RefundAfterRequestDto refundAfterRequestDto);

    @Operation(
        summary = "반품 정책 조회",
        description = "RefundPolicy - 관리자 반품 정책 조회",
        responses = {
            @ApiResponse(
                responseCode = "200"
            )
        }
    )

    @PostMapping("/api/refund/admin/policy/register")
    void saveRefundPolicy (
        @Parameter(description = "반품요청 수락에 필요한 정보")
        @RequestBody RefundPolicyRegisterRequestDto requestDto);

    @Operation(
        summary = "반품 정책 저장",
        description = "RefundPolicy - 관리자 반품 정책 저장",
        responses = {
            @ApiResponse(
                responseCode = "200"
            )
        }
    )
    @GetMapping("/api/refund/admin/policies")
    ResponseEntity<Page<RefundPolicyListResponseDto>> findAllRefundPolicy (
        @Parameter(description = "반품요청 수락에 필요한 정보")
        @RequestParam int page,
        @Parameter(description = "페이지 수")
        @RequestParam int size);
}