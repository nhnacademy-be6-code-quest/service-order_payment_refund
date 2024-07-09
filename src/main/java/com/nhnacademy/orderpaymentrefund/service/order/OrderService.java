package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public interface OrderService {
    PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(long orderId);
    PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(long orderId);
}
