package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.domain.RefundPolicyStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRegisterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyListResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.RefundImpossibleException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundPolicyRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundPolicyService {

    private final RefundPolicyRepository refundPolicyRepository;
    private final OrderRepository orderRepository;

    public Page<RefundPolicyListResponseDto> findPolicies (int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size,
        Sort.by("refundPolicyStatus").ascending());
        Page<RefundPolicy> refundPolicies = refundPolicyRepository.findAll(pageRequest);
        return refundPolicies.map(refundPolicy -> RefundPolicyListResponseDto.builder()
            .refundPolicyType(refundPolicy.getRefundPolicyType())
            .refundShippingFee(refundPolicy.getRefundShippingFee())
            .refundPolicyIssuedDate(String.valueOf(refundPolicy.getRefundPolicyIssuedDate()))
            .refundPolicyStatus(refundPolicy.getRefundPolicyStatus().getValue())
            .build());
    }

    public void saveRefundPolicy (RefundPolicyRegisterRequestDto requestDto){
        RefundPolicy refundPolicy = refundPolicyRepository.findByRefundPolicyType(requestDto.getRefundPolicyType());
        if (refundPolicy != null ){
            refundPolicy.setRefundPolicyOff(RefundPolicyStatus.DISABLED);
            refundPolicyRepository.save(refundPolicy);
        }
        RefundPolicy register = RefundPolicy.builder()
            .refundPolicyType(requestDto.getRefundPolicyType())
            .refundPolicyIssuedDate(LocalDateTime.now())
            .refundShippingFee(requestDto.getRefundShippingFee())
            .refundPolicyStatus(RefundPolicyStatus.ACTIVATE).build();
        refundPolicyRepository.save(register);
    }

    public List<RefundPolicyResponseDto> findRefundPolicy(long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        List<RefundPolicy> refundPolicies;
        LocalDate deliveryStatDate = order.getDeliveryStartDate();
        OrderStatus orderStatus = order.getOrderStatus();
        if (deliveryStatDate != null && orderStatus == OrderStatus.DELIVERING || orderStatus == OrderStatus.DELIVERY_COMPLETE) {
            if (Objects.requireNonNull(deliveryStatDate).isAfter(LocalDate.now().minusDays(10))) {
                refundPolicies = refundPolicyRepository.findByRefundPolicyExpirationDateIsNull();
                return refundPolicies.stream().map(refundPolicy -> new RefundPolicyResponseDto(refundPolicy.getRefundPolicyId(), refundPolicy.getRefundPolicyType()
                    )).toList();
            } else if (deliveryStatDate.isAfter(LocalDate.now().minusDays(30))) {
                refundPolicies = refundPolicyRepository.findByRefundPolicyExpirationDateIsNullAndRefundPolicyTypeNotContaining("단순변심");
                return refundPolicies.stream().map(refundPolicy -> new RefundPolicyResponseDto(refundPolicy.getRefundPolicyId(), refundPolicy.getRefundPolicyType())).toList();
            }else{
                throw new RefundImpossibleException("반품이 불가능합니다.");
            }

        }
         throw new RefundImpossibleException("반품이 불가능합니다.");
    }



}
