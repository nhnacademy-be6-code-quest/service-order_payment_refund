package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.client.payment.TossPaymentsClient;
import com.nhnacademy.orderpaymentrefund.client.refund.TossPayRefundClient;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.TossRefundRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentStrategy;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component(value = "toss")
@RequiredArgsConstructor
public class TossPayment implements PaymentStrategy {

    private final String tossSecretKey;
    private final TossPaymentsClient tossPaymentsClient;
    private final PaymentRepository paymentRepository;
    private final TossPayRefundClient tossPayRefundClient;

    @NoArgsConstructor
    @Getter
    public static class TossApprovePaymentRequestDto {

        String orderId; // uuid
        String paymentKey;
        long amount;

        @Builder
        public TossApprovePaymentRequestDto(String orderId, String paymentKey, long amount){
            this.orderId = orderId;
            this.paymentKey = paymentKey;
            this.amount = amount;
        }
    }

    @Override
    public PaymentsResponseDto approvePayment(
        ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException {

        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(tossSecretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        TossApprovePaymentRequestDto tossApprovePaymentRequestDto = TossApprovePaymentRequestDto.builder()
            .orderId(approvePaymentRequestDto.getOrderCode())
            .paymentKey(approvePaymentRequestDto.getPaymentKey())
            .amount(approvePaymentRequestDto.getAmount())
            .build();

        // 승인 요청을 보내면서 + 응답을 받아 옴.
        String tossPaymentsApproveResponseString = tossPaymentsClient.approvePayment(
            tossApprovePaymentRequestDto, authorizations);

        // 다시 한 번 JSONObject 로 변환한다.
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(
            tossPaymentsApproveResponseString);

        String orderName = jsonObject.get("orderName").toString();
        String totalAmount = jsonObject.get("totalAmount").toString();
        String method = jsonObject.get("method").toString();
        String cardNumber = null;
        String accountNumber = null;
        String bank = null;
        String customerMobilePhone = null;

        if (method.equals("카드")) {
            cardNumber = ((JSONObject) jsonObject.get("card")).get("number").toString();
        } else if (method.equals("가상계좌")) {
            accountNumber = ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber")
                .toString();
        } else if (method.equals("계좌이체")) {
            bank = ((JSONObject) jsonObject.get("transfer")).get("bank").toString();
        } else if (method.equals("휴대폰")) {
            customerMobilePhone = ((JSONObject) jsonObject.get("mobilePhone")).get(
                "customerMobilePhone").toString();
        } else if (method.equals("간편결제")) {
            method =
                method + "-" + ((JSONObject) jsonObject.get("easyPay")).get("provider").toString();
        }

        return PaymentsResponseDto.builder()
            .orderName(orderName)
            .totalAmount(Long.parseLong(totalAmount))
            .method(method)
            .paymentKey(approvePaymentRequestDto.getPaymentKey())
            .cardNumber(cardNumber)
            .accountNumber(accountNumber)
            .bank(bank)
            .customerMobilePhone(customerMobilePhone)
            .orderCode(approvePaymentRequestDto.getOrderCode())
            .methodType(approvePaymentRequestDto.getMethodType())
            .build();

    }

    @Override
    public void refundPayment(long orderId, String cancelReason) {

        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Payment payment = paymentRepository.findByOrder_OrderId(orderId).orElseThrow(()-> new PaymentNotFoundException("결재 정보가 존재하지않습니다."));
        Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(tossSecretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        TossRefundRequestDto dto = TossRefundRequestDto.builder()
            .cancelReason(cancelReason
            ).build();

        tossPayRefundClient.cancelPayment(payment.getPaymentKey(), dto, authorizations);

    }

}
