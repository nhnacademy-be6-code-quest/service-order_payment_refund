package com.nhnacademy.orderpaymentrefund.controller.refund;

import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRequestDto;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

public interface RefundPolicyController {

    @GetMapping("/api/refund/refund-policy")
    List<RefundPolicyRequestDto> findAllRefundPolicyRequestDtoList();
}
