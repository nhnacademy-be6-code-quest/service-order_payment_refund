package com.nhnacademy.orderpaymentrefund.controller.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@WebMvcTest(ClientOrderController.class)
class ClientOrderControllerTest {

    private static final String ID_HEADER = "X-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientOrderService clientOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 주문 임시 저장")
    void saveClientTemporalOrderTest() throws Exception {

        ClientOrderCreateForm clientOrderCreateForm = new ClientOrderCreateForm();

        String body = objectMapper.writeValueAsString(clientOrderCreateForm);

        doNothing().when(clientOrderService)
            .saveClientTemporalOrder(any(HttpHeaders.class), eq(new ClientOrderCreateForm()));

        mockMvc.perform(
                post("/api/client/orders/temporary")
                    .content(body)
                    .header(ID_HEADER, "1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 임시 주문 조회")
    void getClientTemporalOrderTest() throws Exception {

        String tossOrderId = "uuid-1234";

        ClientOrderCreateForm clientOrderCreateForm = new ClientOrderCreateForm();

        when(clientOrderService.getClientTemporalOrder(any(HttpHeaders.class),
            eq(tossOrderId))).thenReturn(
            clientOrderCreateForm);

        mockMvc.perform(get("/api/client/orders/temporary")
                .param("tossOrderId", tossOrderId)
                .header(ID_HEADER, "1")
            )
            .andExpect(status().isOk()); // 상태 코드 확인

    }

    @Test
    @DisplayName("회원 주문 내역 페이지 조회")
    void getOrders() throws Exception {

        Page<ClientOrderGetResponseDto> orderPage = new PageImpl<>(
            Collections.singletonList(new ClientOrderGetResponseDto()));

        when(clientOrderService.getOrders(any(HttpHeaders.class), anyInt(), anyInt(), anyString(),
            anyString())).thenReturn(orderPage);

        mockMvc.perform(
            get("/api/client/orders")
                .header(ID_HEADER, "1")
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원 주문 단건 조회")
    void getOrder() throws Exception {

        ClientOrderGetResponseDto clientOrderGetResponseDto = new ClientOrderGetResponseDto();
        Long orderId = 1L;

        when(clientOrderService.getOrder(any(HttpHeaders.class), anyLong())).thenReturn(
            clientOrderGetResponseDto);

        mockMvc.perform(
            get("/api/client/orders/{orderId}", orderId)
                .header(ID_HEADER, "1")
        ).andExpect(status().isOk());

    }


    @Test
    @DisplayName("회원 주문 상품 상세 리스트 조회")
    void getProductOrderDetailListTest() throws Exception {

        List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList = new ArrayList<>();
        Long orderId = 1L;

        when(clientOrderService.getProductOrderDetailResponseDtoList(any(HttpHeaders.class),
            eq(orderId))).thenReturn(productOrderDetailResponseDtoList);

        mockMvc.perform(
            get("/api/client/orders/{orderId}/detail", orderId)
                .header(ID_HEADER, "1")
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원 주문 상품 상세 단건 조회")
    void getProductOrderDetailTest() throws Exception {

        ProductOrderDetailResponseDto productOrderDetailResponseDto = new ProductOrderDetailResponseDto();
        Long orderId = 1L;
        Long productOrderDetailId = 2L;

        when(clientOrderService.getProductOrderDetailResponseDto(any(HttpHeaders.class), eq(orderId),
            eq(productOrderDetailId))).thenReturn(productOrderDetailResponseDto);

        mockMvc.perform(
            get("/api/client/orders/{orderId}/detail/{productOrderDetailId}", orderId, productOrderDetailId)
                .header(ID_HEADER, "1")
        )
            .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회원 주문 옵션 상품 상세 단건 조회")
    void getProductOrderDetailOptionTest() throws Exception {

        ProductOrderDetailOptionResponseDto productOrderDetailOptionResponseDto = new ProductOrderDetailOptionResponseDto();
        Long orderId = 1L;
        Long productOrderDetailId = 2L;

        when(clientOrderService.getProductOrderDetailOptionResponseDto(any(HttpHeaders.class), eq(orderId),
            eq(productOrderDetailId))).thenReturn(productOrderDetailOptionResponseDto);

        mockMvc.perform(
                get("/api/client/orders/{orderId}/detail/{productOrderDetailId}/option", orderId, productOrderDetailId)
                    .header(ID_HEADER, "1")
            )
            .andExpect(status().isOk());

    }

    @Test
    @DisplayName("주문상태 변경 - 주문취소")
    void cancelOrderTest() throws Exception {
        Long orderId = 1L;

        doNothing().when(clientOrderService).cancelOrder(any(HttpHeaders.class), anyLong());

        mockMvc.perform(
            put("/api/client/orders/{orderId}/cancel", orderId)
                .header(ID_HEADER, "1")
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문상태 변경 - 반품")
    void refundOrderTest() throws Exception {
        Long orderId = 1L;

        doNothing().when(clientOrderService).refundOrder(any(HttpHeaders.class), anyLong());

        mockMvc.perform(
            put("/api/client/orders/{orderId}/refund", orderId)
                .header(ID_HEADER, "1")
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문상태 변경 - 반품")
    void refundRequestOrderTest() throws Exception {
        Long orderId = 1L;

        doNothing().when(clientOrderService).refundOrder(any(HttpHeaders.class), anyLong());

        mockMvc.perform(
            put("/api/client/orders/{orderId}/refund-request", orderId)
                .header(ID_HEADER, "1")
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("주문상태 조회")
    void getOrderStatusTest() throws Exception {
        Long orderId = 1L;

        when(clientOrderService.getOrderStatus(any(HttpHeaders.class), anyLong())).thenReturn("변경된주문상태");

        mockMvc.perform(
            get("/api/client/orders/{orderId}/order-status", orderId)
                .header(ID_HEADER, "1")
        ).andExpect(status().isOk());
    }



}
