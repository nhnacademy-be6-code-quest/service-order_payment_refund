package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundAfterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.PaymentCancelResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.PaymentRefundResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundAdminResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundResultResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundSuccessResponseDto;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundPolicyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 환불 정보를 저장하고, 조회하는 컨트롤러입니다. 환불 정보는 수정되거나 삭제되지 않습니다.
 *
 * @author Virtus_Chae
 * @version 0.0
 */
@RestController()
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;
    private final RefundPolicyService refundPolicyService;


//    @GetMapping("/api/refund")
//    public ResponseEntity<PaymentCancelResponseDto> findPaymentKey(@RequestParam long orderId){
//        return ResponseEntity.ok(refundService.findOrderStatusByOrderId(orderId));
//    }

    @PostMapping("/api/refund/cancel")
    public void paymentCancel(@RequestBody PaymentCancelRequestDto paymentCancelRequestDto){
        refundService.tossRefund(paymentCancelRequestDto.getOrderId(),
            paymentCancelRequestDto.getCancelReason());
        refundService.saveCancel(paymentCancelRequestDto);

    }

    @GetMapping("/api/refund/request")
    public ResponseEntity<List<RefundPolicyResponseDto>> refundRequest(@RequestParam long orderId){
        return ResponseEntity.ok(refundPolicyService.findRefundPolicy(orderId));
    }
    @PostMapping("/api/refund/request")
    public RefundSuccessResponseDto refundRequest(@RequestBody RefundRequestDto requestDto){
        return refundService.saveRefund(requestDto);
    }
    @GetMapping("/api/refund/admin/refund")
    public RefundAdminResponseDto refundAccessView(@RequestParam long orderId) {
        return refundService.findUserRefund(orderId);
    }

    @PostMapping("/api/refund/admin/refund")
    public void refundAccess(@RequestBody RefundAfterRequestDto refundAfterRequestDto){

        RefundResultResponseDto result = refundService.refundUser(refundAfterRequestDto);
        refundService.tossRefund(refundAfterRequestDto.getOrderId(), result.getCancelReason());
    }

}