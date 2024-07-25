package com.nhnacademy.orderpaymentrefund.controller.payment;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 결제 정보를 저장하는 컨트롤러 인터페이스입니다. 결제 정보는 수정되거나 삭제되지 않습니다.
 *
 * @author 김채호
 * @version 1.0
 */
@Tag(name = "Payment", description = "결제 관련 API")
public interface PaymentController {

    @Operation(
        summary = "결재 승인",
        description = "Toss - 결재 승인",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "결재 정보"
            )
        }
    )
    @PostMapping("/api/order/payment/approve")
    PaymentsResponseDto approvePayment(
        @Parameter(description = "결재승인 요청에 필요한 정보")
        @RequestBody ApprovePaymentRequestDto approvePaymentRequestDto)
        throws ParseException;

    @Operation(
        summary = "결재 저장",
        description = "Payment - 사용자 결재정보 저장",
        responses = {
            @ApiResponse(
                responseCode = "200"
            )
        }
    )
    @PostMapping("/api/order/payment/save")
    void savePayment(
        @Parameter(description = "결재정보 저장 정보")
        @RequestBody PaymentsResponseDto paymentsResponseDto,
        @Parameter(description = "회원 정보")
        @RequestHeader HttpHeaders headers);

    @Operation(
        summary = "3개월 결재금액",
        description = "Client - 3개월 순수결재금액 총합",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "3개월 순수 결재금액"
            )
        }
    )
    @GetMapping("/api/payment/grade/{clientId}")
    PaymentGradeResponseDto getPaymentRecordOfClient(
        @Parameter(description = "회원 아이디")
        @PathVariable Long clientId);

    @Operation(
        summary = "결재 후처리 정보",
        description = "Payment - 결재 후 처리를 위한 정보",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "결재 후처리 정보"
            )
        }
    )
    @GetMapping("/api/order/payment/post-process")
    PostProcessRequiredPaymentResponseDto getPostProcessRequiredPaymentResponseDto(
        @Parameter(description = "회원 정보")
        @RequestHeader HttpHeaders headers,
        @Parameter(description = "결재주문아이디")
        @RequestParam("orderCode") String orderCode);

}
