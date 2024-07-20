package com.nhnacademy.orderpaymentrefund.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.controller.order.OrderController;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.InvalidOrderChangeAttempt;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setup() {
    }

    @Test
    @DisplayName("토스 결제 요청을 위한 데이터 조회 성공")
    void getPaymentOrderShowRequestDtoSuccessTest() throws Exception {

        String tossOrderId = "uuid-1234";

        PaymentOrderShowRequestDto resDto = PaymentOrderShowRequestDto.builder()
            .orderTotalAmount(10000L)
            .discountAmountByCoupon(1000L)
            .discountAmountByPoint(500L)
            .tossOrderId(tossOrderId)
            .orderHistoryTitle("초코파이 외 10건")
            .build();

        when(orderService.getPaymentOrderShowRequestDto(any(HttpHeaders.class),
            any(HttpServletRequest.class), eq(tossOrderId))).thenReturn(resDto);

        mockMvc.perform(
            get("/api/order/{tossOrderId}/payment-request", tossOrderId)
                .header("X-User-Id", 181)
        ).andExpect(status().isOk());

    }


    @Test
    @DisplayName("주문상태 실패 테스트")
    void updateOrderStatusFailTest() throws Exception {
        long orderId = 1L;
        String status = "올바르지 않은 주문 상태 변경";

        doThrow(new InvalidOrderChangeAttempt()).when(orderService)
            .changeOrderStatus(orderId, status);

        mockMvc.perform(put("/api/order/{orderId}", orderId)
                .param("status", status)
                .header("X-User-Id", 181))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문상태 성공 테스트")
    void updateOrderStatusSuccessTest() throws Exception {

        long orderId = 1L;
        String status = "올바른 주문 상태 변경";

        doNothing().when(orderService).changeOrderStatus(orderId, status);

        mockMvc.perform(put("/api/order/{orderId}", orderId)
                .param("status", status)
                .header("X-User-Id", 181))
            .andExpect(status().isOk())
            .andExpect(content().string("주문 상태가 변경되었습니다."));

    }


}
