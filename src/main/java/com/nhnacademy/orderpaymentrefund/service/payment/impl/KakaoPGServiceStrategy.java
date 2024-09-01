package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.client.payment.KakaoPaymentClient;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderForm;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.KakaoPaymentReadyRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.PaymentSaveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.KakaoPaymentReadyResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.ApprovePaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.SuccessPaymentOrderInfo;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.impl.KakaoPaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceStrategy;
import com.nhnacademy.orderpaymentrefund.util.OrderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component(value = "kakao")
@RequiredArgsConstructor
public class KakaoPGServiceStrategy implements PGServiceStrategy {

    private final OrderUtil orderUtil;

    private final String kakaoSecretKey;
    private final KakaoPaymentClient kakaoPaymentClient;

    @Override
    public PaymentViewRequestDto getPaymentViewRequestDto(String orderCode) {
        // 결제 준비 요청
        KakaoPaymentReadyResponseDto readyResponseDto = kakaoPaymentReadyProcess(orderCode);
        return KakaoPaymentViewRequestDto.builder()
                .paymentId(readyResponseDto.getTid())
                .redirectUrl(readyResponseDto.getNextRedirectPcUrl())
                .build();
    }

    @Override
    public PaymentApproveResponseDto approvePayment(ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException {
        return null;
    }

    @Override
    public SuccessPaymentOrderInfo getSuccessPaymentOrderInfo(PaymentApproveResponseDto approveResponseDto, Order order, Payment payment) {
        return null;
    }

    @Override
    public void refundPayment(long orderId, String cancelReason) {

    }

    @Override
    public void setPaymentKey(String paymentKey) {

    }

    @Override
    public void setPaymentMethodName(String paymentMethodName) {

    }

    private KakaoPaymentReadyResponseDto kakaoPaymentReadyProcess(String orderCode){

        OrderForm orderForm = orderUtil.getOrderForm(orderCode);
        String itemName = orderUtil.getOrderHistoryTitle(orderForm);

        KakaoPaymentReadyRequestDto requestDto = KakaoPaymentReadyRequestDto.builder()
                .cid(getCid())
                .partnerOrderId(orderCode)
                .partnerUserId("testId")
                .itemName(itemName)
                .quantity(1)
                .totalAmount(orderForm.getTotalPayAmount().intValue())
                .taxFreeAmount(0)
                .approvalUrl(getApprovalUrl(orderCode))
                .cancelUrl(getCancelUrl(orderCode))
                .failUrl(getFailUrl(orderCode))
                .build();

         KakaoPaymentReadyResponseDto responseDto = kakaoPaymentClient.readyKakaoPayment(getHost(), getAuthorization(), requestDto);

        return responseDto;
    }

    private String getCid(){
        return "TC0ONETIME";
    }

    private String getHost(){
        return "open-api.kakaopay.com";
    }

    private String getAuthorization(){
        StringBuilder res = new StringBuilder();
        res.append("SECRET_KEY ");
        res.append(kakaoSecretKey);
        return res.toString();
    }

    private String getContentType(){
        return "application/json";
    }

    private String getApprovalUrl(String orderCode){
        return String.format("https://book-store.shop/client/order/%s/payment/kakao/success", orderCode);
    }

    private String getCancelUrl(String orderCode){
        String url = null;
        try{
            url = URLEncoder.encode(String.format("https://book-store.shop/cancel", orderCode), StandardCharsets.UTF_8.toString());
        }
        catch (UnsupportedEncodingException e){
            log.warn("url 인코딩 중 오류가 발생하였습니다.");
        }
        return "https://book-store.shop/cancel";
    }

    private String getFailUrl(String orderCode){
        return String.format("https://book-store.shop/client/order/%s/payment/fail", orderCode);
    }
}
