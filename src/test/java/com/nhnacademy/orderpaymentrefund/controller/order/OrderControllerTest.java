package com.nhnacademy.orderpaymentrefund.controller.order;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.InvalidOrderChangeAttempt;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@WebMvcTest(OrderControllerImpl.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ClientHeaderContext clientHeaderContext;

    @Test
    @DisplayName("토스 결제 요청을 위한 데이터 조회 성공")
    void getPaymentOrderShowRequestDtoSuccessTest() throws Exception {

        String orderCode = "uuid-1234";

        PaymentOrderShowRequestDto resDto = PaymentOrderShowRequestDto.builder()
            .orderTotalAmount(10000L)
            .discountAmountByCoupon(1000L)
            .discountAmountByPoint(500L)
            .orderCode(orderCode)
            .orderHistoryTitle("초코파이 외 10건")
            .build();

        when(orderService.getPaymentOrderShowRequestDto(eq(orderCode))).thenReturn(resDto);

        mockMvc.perform(
            get("/api/order/{orderCode}/payment-request", orderCode)
                .header("X-User-Id", 181)
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("토스 결제 요청을 위한 데이터 조회 실패")
    void getPaymentOrderShowRequestDtoFailTest() throws Exception {

        String orderCode = "저장되지 않은 uuid-1234";

        doThrow(new OrderNotFoundException()).when(
            orderService).getPaymentOrderShowRequestDto(eq(orderCode));

        mockMvc.perform(
            get("/api/order/{orderCode}/payment-request", orderCode)
        ).andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("토스 결제 승인을 위한 데이터 조회 성공")
    void getPaymentOrderApproveRequestDtoSuccessTest() throws Exception {

        String orderCode = "uuid-1234";

        PaymentOrderApproveRequestDto resDto = PaymentOrderApproveRequestDto.builder()
            .orderTotalAmount(10000L)
            .discountAmountByCoupon(10000L)
            .orderCode(orderCode)
            .clientId(10000L)
            .couponId(10000L)
            .discountAmountByPoint(10000L)
            .accumulatedPoint(10000L)
            .productOrderDetailList(new ArrayList<>())
            .build();

        when(orderService.getPaymentOrderApproveRequestDto(eq(orderCode))).thenReturn(resDto);

        mockMvc.perform(
            get("/api/order/{orderCode}/approve-request", orderCode)
        ).andExpect(status().isOk());


    }

    @Test
    @DisplayName("토스 결제 승인을 위한 데이터 조회 실패")
    void getPaymentOrderApproveRequestDtoFailTest() throws Exception {

        String orderCode = "uuid-1234";

        doThrow(new OrderNotFoundException()).when(orderService)
            .getPaymentOrderApproveRequestDto(eq(orderCode));

        mockMvc.perform(
            get("/api/order/{orderCode}/approve-request", orderCode)
        ).andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("주문 상태변경 실패 테스트")
    void updateOrderStatusFailTest() throws Exception {
        long orderId = 1L;
        String status = "올바르지 않은 주문 상태 변경";

        doThrow(new InvalidOrderChangeAttempt()).when(orderService)
            .changeOrderStatus(orderId, status);

        mockMvc.perform(put("/api/order/{orderId}", orderId)
                .param("status", status))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 상태변경 성공 테스트")
    void updateOrderStatusSuccessTest() throws Exception {

        long orderId = 1L;
        String status = "올바른 주문 상태 변경";

        doNothing().when(orderService).changeOrderStatus(orderId, status);

        mockMvc.perform(put("/api/order/{orderId}", orderId)
                .param("status", status))
            .andExpect(status().isOk())
            .andExpect(content().string("주문 상태가 변경되었습니다."));

    }

    @Test
    @DisplayName("주문 정보 페이지 조회 테스트")
    void getOrderPageSuccessTest() throws Exception {

        Page<OrderResponseDto> orderPage = new PageImpl<>(
            Collections.singletonList(new OrderResponseDto()));
        when(orderService.getAllOrderList(anyInt(), anyInt(), anyString(), anyString())).thenReturn(
            orderPage);

        mockMvc.perform(
                get("/api/order/all")
                    .param("pageSize", "10")
                    .param("pageNo", "10")
                    .param("sortBy", "current")
                    .param("sortDir", "asc"))
            .andExpect(status().isOk());

    }

    @Test
    @DisplayName("주문한 상품 상세 조회 테스트")
    void getOrderProductDetailResponseDtoListTest() throws Exception {

        List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList = new ArrayList<>();
        long orderId = 1L;

        when(orderService.getProductOrderDetailList(anyLong())).thenReturn(
            productOrderDetailResponseDtoList);

        mockMvc.perform(
                get("/api/order/{orderId}/detail", orderId))
            .andExpect(status().isOk());


    }

    @Test
    @DisplayName("주문한 상품 상세 조회 테스트 - 주문상품 상세 아이디로 조회")
    void getOrderProductDetailResponseDtoByProductOrderDetailIdTest() throws Exception {

        ProductOrderDetailResponseDto productOrderDetailResponseDto = new ProductOrderDetailResponseDto();
        long orderId = 1L;
        long productOrderDetailId = 1L;

        when(orderService.getProductOrderDetail(anyLong(), anyLong())).thenReturn(
            productOrderDetailResponseDto);

        mockMvc.perform(
                get("/api/order/{orderId}/detail/{productOrderDetailId}", orderId,
                    productOrderDetailId))
            .andExpect(status().isOk());

    }

    @Test
    @DisplayName("주문한 상품의 옵션상품 상세 조회 테스트 - 주문 상품 상세 아이디로 조회")
    void productOrderDetailResponseDtoTest() throws Exception {

        ProductOrderDetailOptionResponseDto productOrderDetailOptionResponseDto = new ProductOrderDetailOptionResponseDto();

        long orderId = 1L;
        long productOrderDetailId = 1L;

        when(orderService.getProductOrderDetailOptionResponseDto(anyLong(), anyLong())).thenReturn(
            productOrderDetailOptionResponseDto);

        mockMvc.perform(
                get("/api/order/{orderId}/detail/{productOrderDetailId}/option", orderId,
                    productOrderDetailId)
                    .header("X-User-Id", 181))
            .andExpect(status().isOk());

    }


}
