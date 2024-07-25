package com.nhnacademy.orderpaymentrefund.service.paymentsupplier.paymentstrategyimpl;

import com.nhnacademy.orderpaymentrefund.client.payment.TossPaymentsClient;
import com.nhnacademy.orderpaymentrefund.client.refund.TossPayRefundClient;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.TossPaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.TossRefundRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.paymentsupplier.PaymentStrategy;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component("toss")
@RequiredArgsConstructor
public class TossPayment implements PaymentStrategy {
    private final TossPaymentsClient tossPaymentsClient;
    private final String tossSecretKey;
    private final TossPayRefundClient tossPayRefundClient;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentsResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto)
        throws ParseException {
        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(tossSecretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);
        TossPaymentRequestDto tossPaymentRequestDto = new TossPaymentRequestDto(approvePaymentRequestDto.getOrderId(), approvePaymentRequestDto.getAmount(), approvePaymentRequestDto.getPaymentKey());
        // 승인 요청을 보내면서 + 응답을 받아 옴.
        String tossPaymentsApproveResponseString = tossPaymentsClient.approvePayment(
            tossPaymentRequestDto, authorizations);

        // 다시 한 번 JSONObject 로 변환한다.
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(
            tossPaymentsApproveResponseString);

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
            .totalAmount(Long.parseLong(totalAmount))
            .method(method)
            .paymentKey(approvePaymentRequestDto.getPaymentKey())
            .cardNumber(cardNumber)
            .accountNumber(accountNumber)
            .bank(bank)
            .customerMobilePhone(customerMobilePhone)
            .orderId(approvePaymentRequestDto.getOrderId())
            .build();
    }



    @Override
    public void paymentRefund(long orderId, String cancelReason) {

        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Payment payment = paymentRepository.findByOrder_OrderId(orderId).orElseThrow(()-> new PaymentNotFoundException("결재 정보가 존재하지않습니다."));
        Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(tossSecretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        TossRefundRequestDto dto = TossRefundRequestDto.builder()
            .cancelReason(cancelReason
            ).build();

        tossPayRefundClient.cancelPayment(payment.getTossPaymentKey(), dto, authorizations);

    }
}
