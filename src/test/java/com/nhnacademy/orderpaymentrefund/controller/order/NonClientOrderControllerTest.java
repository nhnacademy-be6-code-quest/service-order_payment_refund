package com.nhnacademy.orderpaymentrefund.controller.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import({SecurityConfig.class, HeaderFilter.class})
@WebMvcTest(NonClientOrderControllerImpl.class)
class NonClientOrderControllerTest {

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

        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();
        String body = objectMapper.writeValueAsString(nonClientOrderForm);

        doNothing().when(nonClientOrderService)
            .saveNonClientTemporalOrder(any(HttpHeaders.class), any(NonClientOrderForm.class));

        mockMvc.perform(post("/api/non-client/orders/temporary")
                .headers(headers)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비회원 임시 주문 조회 테스트")
    void getNonClientTemporalOrderTest() throws Exception {

        HttpHeaders headers = new HttpHeaders();

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

        Long orderId=  1L;
        String pwd = "비밀번호486";

        NonClientOrderGetResponseDto nonClientOrderGetResponseDto = new NonClientOrderGetResponseDto();

        when(nonClientOrderService.getOrder(any(HttpHeaders.class), anyLong(), anyString())).thenReturn(nonClientOrderGetResponseDto);

        mockMvc.perform(
            get("/api/non-client/orders/{orderId}", orderId)
                .param("pwd", pwd)

        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("비회원 주문 아이디 찾기 페이지 테스트")
    void findNonClientOrderIdTest() throws Exception {

        HttpHeaders headers = new HttpHeaders();

        Page<FindNonClientOrderIdInfoResponseDto> orderPage = new PageImpl<>(
            Collections.singletonList(FindNonClientOrderIdInfoResponseDto.builder().build()));

        when(nonClientOrderService.findNonClientOrderId(any(HttpHeaders.class), any(
            FindNonClientOrderIdRequestDto.class), any(PageRequest.class)))
            .thenReturn(orderPage);

        mockMvc.perform(
            get("/api/non-client/orders/find-orderId")
                .param("pageSize", "10")
                .param("pageNo", "0")
                .param("sortBy", "orderDatetime")
                .param("sortDir", "asc")
                .headers(headers)
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("비회원 주문 비밀번호 테스트")
    void findNonClientOrderPasswordTest() throws Exception {

        HttpHeaders headers = new HttpHeaders();

        Long orderId = 1L;
        String findPassword = "찾은 비밀번호";

        when(nonClientOrderService.findNonClientOrderPassword(any(HttpHeaders.class), any(FindNonClientOrderPasswordRequestDto.class))).thenReturn(findPassword);

        mockMvc.perform(
            get("/api/non-client/orders/find-password")
                .headers(headers)
                .param("orderId", String.valueOf(orderId))
                .param("ordererName", "홍길동")
                .param("phoneNumber", "01012341234")
                .param("email", "gildong@test.com")
        ).andExpect(status().isOk());

    }

}
