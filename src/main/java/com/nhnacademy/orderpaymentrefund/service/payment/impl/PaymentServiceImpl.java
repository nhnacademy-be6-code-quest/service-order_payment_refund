package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    // Order Enum Type -> String, 배송 상태 -> tinyInt
    @Override
    public void savePayment(long orderId, TossPaymentsResponseDto tossPaymentsResponseDto) {
        Payment payment = Payment.builder()
            .order(
                orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new))
            .payAmount(tossPaymentsResponseDto.getTotalAmount())
            .payTime(LocalDateTime.now())
            .paymentMethodName(tossPaymentsResponseDto.getMethod())
            .tossPaymentKey(tossPaymentsResponseDto.getPaymentKey())
            .build();
        paymentRepository.save(payment);
    }

    @Override
    public PaymentGradeResponseDto getPaymentRecordOfClient(Long clientId) {
        Long totalOptionPriceForLastThreeMonth = orderRepository.getTotalOptionPriceForLastThreeMonths(
            clientId, LocalDateTime.now().minusDays(90L));
        if (totalOptionPriceForLastThreeMonth == null) {
            totalOptionPriceForLastThreeMonth = 0L;
        }
        log.error("totalOptionPriceForLastThreeMonth: {}", totalOptionPriceForLastThreeMonth);
        return PaymentGradeResponseDto.builder()
            .paymentGradeValue(
                orderRepository.sumFinalAmountForCompletedOrders(clientId,
                    LocalDateTime.now().minusDays(90L))
                    - totalOptionPriceForLastThreeMonth)
            .build();
    }
}