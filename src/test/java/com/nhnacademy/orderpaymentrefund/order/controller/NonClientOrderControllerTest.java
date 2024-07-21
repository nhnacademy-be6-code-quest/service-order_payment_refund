package com.nhnacademy.orderpaymentrefund.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.controller.order.NonClientOrderController;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import java.util.Collections;
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

@Import({SecurityConfig.class, HeaderFilter.class})
@WebMvcTest(NonClientOrderController.class)
class NonClientOrderControllerTest {

    private static final String ID_HEADER = "X-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NonClientOrderService nonClientOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("비회원 주문 임시 저장 테스트")
    void saveNonClientTemporalOrderTest() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add(ID_HEADER, "비회원");

        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        String body = objectMapper.writeValueAsString(nonClientOrderForm);

        doNothing().when(nonClientOrderService)
            .saveNonClientTemporalOrder(any(HttpHeaders.class), eq(nonClientOrderForm));

        mockMvc.perform(
                post("/api/non-client/orders/temporary")
                    .headers(headers)
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비회원 임시 주문 조회 테스트")
    void getNonClientTemporalOrderTest() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add(ID_HEADER, "비회원");

        String tossOrderId = "uuid-1234";

        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        when(nonClientOrderService.getNonClientTemporalOrder(any(HttpHeaders.class),
            anyString())).thenReturn(nonClientOrderForm);

        mockMvc.perform(
            get("/api/non-client/orders/temporary")
                .headers(headers)
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("비회원 주문 조회 테스트")
    void findNonClientOrderTest() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.add(ID_HEADER, "비회원");

        Long orderId=  1L;
        String pwd = "비밀번호486";

        NonClientOrderGetResponseDto nonClientOrderGetResponseDto = new NonClientOrderGetResponseDto();

        when(nonClientOrderService.getOrder(any(HttpHeaders.class), anyLong(), anyString())).thenReturn(nonClientOrderGetResponseDto);

        mockMvc.perform(
            get("/api/non-client/orders/{orderId}", orderId)
                .param(pwd)
                .headers(headers)
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("비회원 주문 아이디 찾기 페이지 테스트")
    void findNonClientOrderIdTest() {
        Page<FindNonClientOrderIdInfoResponseDto> orderPage = new PageImpl<>(
            Collections.singletonList(FindNonClientOrderIdInfoResponseDto.builder().build()));
//
//        mockMvc.perform(
//            get("/api/non-client/orders/find-orderId")
//                .param()
//        )
    }

    @Test
    @DisplayName("비회원 주문 비밀번호 테스트")
    void findNonClientOrderPasswordTest() {

    }

    @Test
    @DisplayName("비회원 주문 상태 변경 - 결제완료")
    void paymentCompleteOrderTest() {

    }


}
