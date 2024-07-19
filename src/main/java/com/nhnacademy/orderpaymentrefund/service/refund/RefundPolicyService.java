package com.nhnacademy.orderpaymentrefund.service.refund;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.Refund;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
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

    public List<RefundPolicyRequestDto> findAllRefundPolicyRequestDtoList() {
        List<RefundPolicyRequestDto> refundPolicyRequestDtoList = new ArrayList<>();

        List<RefundPolicy> refundPolicyList = refundPolicyRepository.findAll();
        for (RefundPolicy refundPolicy : refundPolicyList) {
            refundPolicyRequestDtoList.add(
                RefundPolicyRequestDto.builder()
                    .refundAndCancelPolicyType(refundPolicy.getRefundPolicyType())
                    .refundShippingFee(refundPolicy.getRefundShippingFee())
                    .build()
            );
        }
        return refundPolicyRequestDtoList;
    }

    public void save(RefundPolicyRegisterRequestDto requestDto){
        RefundPolicy refund = refundPolicyRepository.findByRefundPolicyType(requestDto.getRefundPolicyType());
        if (refund != null){
        refund.setRefundPolicyExpirationDate(LocalDateTime.now());
        refundPolicyRepository.save(refund);
        }

        RefundPolicy refundPolicy = new RefundPolicy(requestDto.getRefundPolicyType(), requestDto.getRefundShippingFee());
        refundPolicyRepository.save(refundPolicy);
    }



    public void refundStatus(long oderId){
        Order order = orderRepository.findById(oderId).orElseThrow(()-> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        order.updateOrderStatus(OrderStatus.REFUND_REQUEST);
        orderRepository.save(order);
    }


    public List<RefundPolicyResponseDto> findRefundPolicy(long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
        List<RefundPolicy> refundPolicies;
        LocalDate deliveryStatDate = order.getDeliveryStartDate();
        OrderStatus orderStatus = order.getOrderStatus();
        if (deliveryStatDate != null && orderStatus == OrderStatus.DELIVERING || orderStatus == OrderStatus.DELIVERY_COMPLETE) {
            if (deliveryStatDate.isAfter(LocalDate.now().minusDays(10))) {
                refundPolicies = refundPolicyRepository.findByRefundPolicyExpirationDateIsNotNull();
            } else if (LocalDate.now().isEqual(deliveryStatDate.minusDays(30))) {
                refundPolicies = refundPolicyRepository.findByRefundPolicyExpirationDateIsNotNullAndRefundPolicyTypeNotContaining("단순변심");
            }else{
                //10일전ㅌㅌ
                //30일전
                throw new RefundImpossibleException("반품이 불가능합니다.");

            }
            return refundPolicies.stream().map(refundPolicy -> {
                RefundPolicyResponseDto dto = new RefundPolicyResponseDto();
                dto.setRefundPolicyId(refundPolicy.getRefundPolicyId());
                dto.setRefundPolicyType(refundPolicy.getRefundPolicyType());
                dto.setRefundShippingFee(refundPolicy.getRefundShippingFee());
                return dto;
            }).toList();
        }
         throw new RefundImpossibleException("반품이 불가능합니다.");
    }



}
