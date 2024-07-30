package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentStrategy;
import org.json.simple.parser.ParseException;

public class KakaoPayment implements PaymentStrategy {

    // TODO 자식클래스 반환은 되는데 왜 파라미터는 안되는가????

    @Override
    public PaymentsResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto)
        throws ParseException {
        System.out.println("1");
        return null;
    }

//    // 오버로드
//    public PaymentsResponseDto approvePayment(KakaoApprovePaymentRequestDto approvePaymentRequestDto)
//        throws ParseException {
//        System.out.println("2");
//        return null;
//    }
//


    @Override
    public void refundPayment(long orderId, String cancelReason) {

    }
}

class A {
    public static void main(String[] args) throws ParseException {
        PaymentStrategy paymentStrategy = new KakaoPayment();

        ApprovePaymentRequestDto approvePaymentRequestDto = new ApprovePaymentRequestDto();
        KakaoApprovePaymentRequestDto kakaoApprovePaymentRequestDto = new KakaoApprovePaymentRequestDto();

        paymentStrategy.approvePayment(approvePaymentRequestDto);
        paymentStrategy.approvePayment(kakaoApprovePaymentRequestDto);
    }
}

// 파라미터를 인터페이스로, 실제 넘기는 인자는 구현체로??! 반환도 마찬가지. 근데, 보통 반환할때 구현체로 명시하긴 함.