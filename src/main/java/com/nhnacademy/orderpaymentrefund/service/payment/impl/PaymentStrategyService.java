package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.SuccessPaymentOrderInfo;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.type.InternalServerExceptionType;
import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStrategyService {

    private final PGServiceStrategyProvider pgServiceStrategyProvider;

    public PaymentViewRequestDto getPaymentViewRequestDto(String pgName, String orderCode) {
        PGServiceStrategy pgServiceStrategy = pgServiceStrategyProvider.getPaymentStrategy(pgName);
        return pgServiceStrategy.getPaymentViewRequestDto(orderCode);
    }

    public PaymentApproveResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto) {

        PGServiceStrategy pgServiceStrategy = pgServiceStrategyProvider.getPaymentStrategy(
                approvePaymentRequestDto.getPgName().toLowerCase());

        try{
            return pgServiceStrategy.approvePayment(approvePaymentRequestDto);
        } catch (ParseException e) {
            log.error("PG 서비스의 승인 요청 응답을 json으로 변환하는 과정에서 파싱오류가 발생하였습니다.");
            throw new InternalServerExceptionType("json 파싱 오류");
        }

    }

    public SuccessPaymentOrderInfo getSuccessPaymentOrderInfo(String pgName, PaymentApproveResponseDto paymentApproveResponseDto, Order order, Payment payment) {
        PGServiceStrategy pgServiceStrategy = pgServiceStrategyProvider.getPaymentStrategy(pgName);
        return pgServiceStrategy.getSuccessPaymentOrderInfo(paymentApproveResponseDto, order, payment);
    }

    public void refundPayment(String paymentType, long orderId, String cancelReason){

        PGServiceStrategy pgServiceStrategy = pgServiceStrategyProvider.getPaymentStrategy(paymentType.toLowerCase());

        pgServiceStrategy.refundPayment(orderId, cancelReason);

    }

}
