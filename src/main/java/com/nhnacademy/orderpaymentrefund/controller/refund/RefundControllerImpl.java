package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundAfterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundResultResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundSuccessResponseDto;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundPolicyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
public class RefundControllerImpl implements RefundController{

    private final RefundService refundService;
    private final RefundPolicyService refundPolicyService;

    @Override
    public void paymentCancel(@RequestBody PaymentCancelRequestDto paymentCancelRequestDto){
        refundService.tossRefund(paymentCancelRequestDto.getOrderId(),
            paymentCancelRequestDto.getCancelReason());
        refundService.saveCancel(paymentCancelRequestDto);

    }

    @Override
    public ResponseEntity<List<RefundPolicyResponseDto>> refundRequest(@RequestParam long orderId){
        return ResponseEntity.ok(refundPolicyService.findRefundPolicy(orderId));
    }

    @Override
    public RefundSuccessResponseDto refundRequest(@RequestBody RefundRequestDto requestDto){
        return refundService.saveRefund(requestDto);
    }

    @Override
    public void refundAccess(@RequestBody RefundAfterRequestDto refundAfterRequestDto){
        RefundResultResponseDto result = refundService.refundUser(refundAfterRequestDto);
        refundService.tossRefund(refundAfterRequestDto.getOrderId(), result.getCancelReason());
    }

}