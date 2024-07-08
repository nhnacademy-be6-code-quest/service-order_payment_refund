package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public interface OrderService {
    PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(HttpHeaders headers, long orderId);
    PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(HttpHeaders headers, long orderId);
}
