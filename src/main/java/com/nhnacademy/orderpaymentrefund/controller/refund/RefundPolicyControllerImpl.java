package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRequestDto;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundPolicyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundPolicyControllerImpl {

    private final RefundPolicyService refundPolicyService;

    @GetMapping("/api/refund/refund-policy")
    List<RefundPolicyRequestDto> findAllRefundPolicyRequestDtoList() {
        return refundPolicyService.findAllRefundPolicyRequestDtoList();
    }
}