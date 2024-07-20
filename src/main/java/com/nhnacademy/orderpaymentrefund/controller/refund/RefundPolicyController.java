package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundPolicyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundPolicyController {

    private final RefundPolicyService refundPolicyService;
    private final RefundService refundService;

    @GetMapping("/api/refund/refund-policy")
    List<RefundPolicyRequestDto> findAllRefundPolicyRequestDtoList() {
        return refundPolicyService.findAllRefundPolicyRequestDtoList();
    }





}