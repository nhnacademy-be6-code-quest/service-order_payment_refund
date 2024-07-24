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
import com.nhnacademy.orderpaymentrefund.dto.payment.request.TossApprovePaymentRequest;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.UserUpdateGradeRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import com.nhnacademy.orderpaymentrefund.service.payment.PaymentService;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("결제 승인 성공")
    void approvePaymentTest() throws Exception {
        TossApprovePaymentRequest request = new TossApprovePaymentRequest("order123", 1000,
            "paymentKey123");
        TossPaymentsResponseDto response = TossPaymentsResponseDto.builder()
            .orderName("Order Name")
            .totalAmount(1000)
            .method("카드")
            .paymentKey("paymentKey123")
            .orderId("order123")
            .build();

        when(paymentService.approvePayment(any(TossApprovePaymentRequest.class))).thenReturn(
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
        TossPaymentsResponseDto request = TossPaymentsResponseDto.builder()
            .orderName("Order Name")
            .totalAmount(1000)
            .method("카드")
            .paymentKey("paymentKey123")
            .orderId("order123")
            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer token");

        doNothing().when(paymentService)
            .savePayment(any(HttpHeaders.class), any(TossPaymentsResponseDto.class));

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
