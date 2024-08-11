package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundAfterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRegisterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.PaymentMethodResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyListResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundResultResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundSuccessResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.impl.PaymentStrategyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundPolicyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final PaymentStrategyService paymentStrategyService;

    @Override
    public void paymentCancel(@RequestBody PaymentCancelRequestDto paymentCancelRequestDto){
        PaymentMethodResponseDto dto = refundService.findPayMethod(paymentCancelRequestDto.getOrderId());
        paymentStrategyService.refundPayment(dto.getMethodType(), paymentCancelRequestDto.getOrderId(),
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
        paymentStrategyService.refundPayment(result.getMethodType(), refundAfterRequestDto.getOrderId(), result.getCancelReason());
    }
    @Override
    public void saveRefundPolicy (@RequestBody RefundPolicyRegisterRequestDto requestDto){
        refundPolicyService.saveRefundPolicy(requestDto);
    }
    @Override
    public ResponseEntity<Page<RefundPolicyListResponseDto>> findAllRefundPolicy (@RequestParam int page, @RequestParam int size){
        return ResponseEntity.ok(refundPolicyService.findPolicies(page, size));
    }

}