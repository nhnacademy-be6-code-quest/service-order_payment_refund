package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
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

        List<RefundPolicy> refundPolicyList = refundPolicyRepository.findAll();
        for (RefundPolicy refundPolicy : refundPolicyList) {
            refundPolicyRequestDtoList.add(
                RefundPolicyRequestDto.builder()
                    .refundAndCancelPolicyReason(
                        refundPolicy.getRefundPolicyReason())
                    .refundAndCancelPolicyType(refundPolicy.getRefundPolicyType())
                    .refundShippingFee(refundPolicy.getRefundShippingFee())
                    .build()
            );
        }

        return refundPolicyRequestDtoList;
    }
}
