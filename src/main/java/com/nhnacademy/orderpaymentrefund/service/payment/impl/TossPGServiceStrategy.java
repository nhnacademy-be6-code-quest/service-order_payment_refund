package com.nhnacademy.orderpaymentrefund.service.payment.impl;

import com.nhnacademy.orderpaymentrefund.client.payment.TossPaymentsClient;
import com.nhnacademy.orderpaymentrefund.client.refund.TossPayRefundClient;
import com.nhnacademy.orderpaymentrefund.context.PaymentContext;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.ApproveTossPayRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.PaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.SuccessPaymentOrderInfo;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.approve.impl.TossPaymentApproveResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.impl.TossPaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.TossRefundRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.payment.PGServiceStrategy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;

import com.nhnacademy.orderpaymentrefund.util.OrderUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component(value = "toss")
@RequiredArgsConstructor
public class TossPGServiceStrategy implements PGServiceStrategy {

    private final PaymentContext paymentContext;

    private final OrderUtil orderUtil;
    private final String tossSecretKey;
    private final TossPaymentsClient tossPaymentsClient;
    private final PaymentRepository paymentRepository;
    private final TossPayRefundClient tossPayRefundClient;

    private final ProductOrderDetailRepository productOrderDetailRepository;

    @Override
    public PaymentViewRequestDto getPaymentViewRequestDto(String orderCode) {
        OrderForm orderForm = orderUtil.getOrderForm(orderCode);
        String orderHistoryTitle = orderUtil.getOrderHistoryTitle(orderForm);
        long payTotalAmount = orderForm.getTotalPayAmount();

        return TossPaymentViewRequestDto.builder()
                .amount(payTotalAmount)
                .orderCode(orderCode)
                .orderName(orderHistoryTitle)
                .build();

    }

    @Override
    public PaymentApproveResponseDto approvePayment(
            ApprovePaymentRequestDto approvePaymentRequestDto) throws ParseException {

        String orderCode = approvePaymentRequestDto.getOrderCode();

        // 결제금액 변조 확인
        long amount = Long.parseLong(approvePaymentRequestDto.getReqParamMap().get("amount")[0]);
        if(!isValidTotalPayAmount(orderCode, amount)){
            throw new BadRequestExceptionType("결제금액 변조가 의심됩니다");
        }

        // 시크릿 키를 Base64로 인코딩하여 Authorization 헤더 생성
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(tossSecretKey.getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        String paymentKey = approvePaymentRequestDto.getReqParamMap().get("paymentKey")[0];
        setPaymentKey(paymentKey);

        // 승인에 필요한 요청객체
        ApproveTossPayRequestDto approveTossPayRequestDto = ApproveTossPayRequestDto.builder()
            .orderId(orderCode)
            .paymentKey(paymentKey)
            .amount(amount)
            .build();

        // 승인 응답
        TossPaymentApproveResponseDto tossPaymentApproveResponseDto = tossPaymentsClient.approvePayment(approveTossPayRequestDto, authorizations);
        setPaymentMethodName(tossPaymentApproveResponseDto.getMethod());

        return tossPaymentApproveResponseDto;

    }

    @Override
    public SuccessPaymentOrderInfo getSuccessPaymentOrderInfo(PaymentApproveResponseDto approveResponseDto, Order order, Payment payment) {
        if(approveResponseDto instanceof TossPaymentApproveResponseDto tossPaymentApproveResponseDto){
            SuccessPaymentOrderInfo.VirtualAccountInfo virtualAccountInfo = null;
            if(tossPaymentApproveResponseDto.isVirtualAccount()){

                TossPaymentApproveResponseDto.VirtualAccount virtualAccount = tossPaymentApproveResponseDto.getVirtualAccount();

                StringBuilder depositDueDate = new StringBuilder();
                depositDueDate.append(virtualAccount.getDueDate().getYear()).append("년 ");
                depositDueDate.append(virtualAccount.getDueDate().getMonth()).append("월 ");
                depositDueDate.append(virtualAccount.getDueDate().getDayOfMonth()).append("일 ");
                depositDueDate.append(virtualAccount.getDueDate().getHour()).append(":").append(virtualAccount.getDueDate().getMinute()).append(":").append(virtualAccount.getDueDate().getSecond());

                virtualAccountInfo = SuccessPaymentOrderInfo.VirtualAccountInfo.builder()
                        .bank(virtualAccount.getBankCode())
                        .account(virtualAccount.getAccountNumber())
                        .depositDueDate(depositDueDate.toString())
                        .build();

            }

            List<Long> productIdList = new ArrayList<>();
            List<ProductOrderDetail> productOrderDetailList = productOrderDetailRepository.findAllByOrder_OrderId(order.getOrderId());
            for(ProductOrderDetail productOrderDetail : productOrderDetailList){
                productIdList.add(productOrderDetail.getProductId());
            }

            return SuccessPaymentOrderInfo.builder()
                    .orderIdOnDB(order.getOrderId())
                    .productIdList(productIdList)
                    .orderName(orderUtil.getOrderHistoryTitle(productOrderDetailList))
                    .totalPayAmount(payment.getPayAmount())
                    .deliveryAddress(order.getDeliveryAddress())
                    .paymentMethodName(payment.getPaymentMethodName())
                    .virtualAccountInfo(virtualAccountInfo)
                    .build();
        }
        throw new BadRequestExceptionType("파라미터 PaymentApproveResponseDto의 구현체가 TossPaymentApproveResponseDto여야 합니다.");
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

    @Override
    public void setPaymentKey(String paymentKey) {
        paymentContext.setPaymentKey(paymentKey);
    }

    @Override
    public void setPaymentMethodName(String paymentMethodName) {
        paymentContext.setPaymentMethodName(paymentMethodName);
    }

    private boolean isValidTotalPayAmount(String orderCode, Long amount){
        OrderForm orderForm = orderUtil.getOrderForm(orderCode);
        return amount != null && orderForm.getTotalPayAmount().equals(amount);
    }

}
