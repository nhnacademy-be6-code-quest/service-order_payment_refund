package com.nhnacademy.orderpaymentrefund.controller.payment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
import com.nhnacademy.orderpaymentrefund.service.payment.impl.PaymentStrategyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import({SecurityConfig.class, HeaderFilter.class})
@WebMvcTest(PaymentControllerImpl.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private PaymentStrategyService paymentStrategyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("결제 승인 성공")
    void approvePaymentTest() throws Exception {
        ApprovePaymentRequestDto request = new ApprovePaymentRequestDto("order123", 1000,
            "paymentKey123","toss");
        PaymentsResponseDto response = PaymentsResponseDto.builder()
            .orderName("Order Name")
            .totalAmount(1000)
            .method("카드")
            .paymentKey("paymentKey123")
            .orderCode("order123")
            .build();

        when(paymentStrategyService.approvePayment(any(ApprovePaymentRequestDto.class))).thenReturn(
            response);

        mockMvc.perform(post("/api/order/payment/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderName").value("Order Name"))
            .andExpect(jsonPath("$.totalAmount").value(1000))
            .andExpect(jsonPath("$.method").value("카드"))
            .andExpect(jsonPath("$.paymentKey").value("paymentKey123"))
            .andExpect(jsonPath("$.orderId").value("order123"));
    }

    @Test
    @DisplayName("결제 저장 성공")
    void savePaymentTest() throws Exception {
        PaymentsResponseDto request = PaymentsResponseDto.builder()
            .orderName("Order Name")
            .totalAmount(1000)
            .method("카드")
            .paymentKey("paymentKey123")
            .orderCode("order123")
            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer token");

        doNothing().when(paymentService)
            .savePayment(any(HttpHeaders.class), any(PaymentsResponseDto.class));

        mockMvc.perform(post("/api/order/payment/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .headers(headers))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("3개월 결제 금액 조회 성공")
    void getPaymentRecordOfClientTest() throws Exception {
        PaymentGradeResponseDto response = new PaymentGradeResponseDto(5000L);

        when(paymentService.getPaymentRecordOfClient(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/payment/grade/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.paymentGradeValue").value(5000));
    }

    @Test
    @DisplayName("결제 후처리 정보 조회 성공")
    void getPostProcessRequiredPaymentResponseDtoTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        PostProcessRequiredPaymentResponseDto response = PostProcessRequiredPaymentResponseDto.builder()
            .orderId(123L)
            .clientId(456L)
            .amount(1000)
            .paymentMethodName("카드")
            .build();

        response.addProductIdList(1L);
        response.addProductIdList(2L);
        response.addProductIdList(3L);

        when(paymentService.getPostProcessRequiredPaymentResponseDto(headers, anyString())).thenReturn(
            response);

        mockMvc.perform(get("/api/order/payment/post-process")
                .param("orderCode", "order123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value(123))
            .andExpect(jsonPath("$.clientId").value(456))
            .andExpect(jsonPath("$.amount").value(1000))
            .andExpect(jsonPath("$.paymentMethodName").value("카드"))
            .andExpect(jsonPath("$.productIdList[0]").value(1))
            .andExpect(jsonPath("$.productIdList[1]").value(2))
            .andExpect(jsonPath("$.productIdList[2]").value(3));
    }
}
