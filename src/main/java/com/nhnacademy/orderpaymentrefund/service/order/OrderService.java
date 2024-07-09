package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderListGetResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public interface OrderService {
    PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(long orderId);
    PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(long orderId);
    void changeOrderStatus(long orderId, String orderStatus);
}
