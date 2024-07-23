package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRegisterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.RefundImpossibleException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundPolicyRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundPolicyService {

    private final RefundPolicyRepository refundPolicyRepository;
    private final OrderRepository orderRepository;

    public List<RefundPolicyResponseDto> findRefundPolicy(long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        List<RefundPolicy> refundPolicies;
        LocalDate deliveryStatDate = order.getDeliveryStartDate();
        OrderStatus orderStatus = order.getOrderStatus();
        if (deliveryStatDate != null && orderStatus == OrderStatus.DELIVERING || orderStatus == OrderStatus.DELIVERY_COMPLETE) {
            if (deliveryStatDate.isAfter(LocalDate.now().minusDays(10))) {
                refundPolicies = refundPolicyRepository.findByRefundPolicyExpirationDateIsNull();
                return refundPolicies.stream().map(refundPolicy -> new RefundPolicyResponseDto(refundPolicy.getRefundPolicyId(), refundPolicy.getRefundPolicyType(),
                    refundPolicy.getRefundShippingFee())).toList();
            } else if (deliveryStatDate.isAfter(LocalDate.now().minusDays(30))) {
                refundPolicies = refundPolicyRepository.findByRefundPolicyExpirationDateIsNullAndRefundPolicyTypeNotContaining("단순변심");
                return refundPolicies.stream().map(refundPolicy -> new RefundPolicyResponseDto(refundPolicy.getRefundPolicyId(), refundPolicy.getRefundPolicyType(),
                    refundPolicy.getRefundShippingFee())).toList();
            }else{
                throw new RefundImpossibleException("반품이 불가능합니다.");
            }

        }
         throw new RefundImpossibleException("반품이 불가능합니다.");
    }



}
