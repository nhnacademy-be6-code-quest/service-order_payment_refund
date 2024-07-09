package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundAndCancelPolicy;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRequestDto;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundPolicyRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundPolicyService {

    private final RefundPolicyRepository refundPolicyRepository;

    public List<RefundPolicyRequestDto> findAllRefundPolicyRequestDtoList() {
        List<RefundPolicyRequestDto> refundPolicyRequestDtoList = new ArrayList<>();

        List<RefundAndCancelPolicy> refundAndCancelPolicyList = refundPolicyRepository.findAll();
        for (RefundAndCancelPolicy refundAndCancelPolicy : refundAndCancelPolicyList) {
            refundPolicyRequestDtoList.add(
                RefundPolicyRequestDto.builder()
                    .refundAndCancelPolicyReason(
                        refundAndCancelPolicy.getRefundAndCancelPolicyReason())
                    .refundAndCancelPolicyType(refundAndCancelPolicy.getRefundAndCancelPolicyType())
                    .refundShippingFee(refundAndCancelPolicy.getRefundShippingFee())
                    .build()
            );
        }

        return refundPolicyRequestDtoList;
    }
}
