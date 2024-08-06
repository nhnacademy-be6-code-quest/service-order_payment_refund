package com.nhnacademy.orderpaymentrefund.controller.refund;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundAfterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRegisterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.PaymentMethodResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyListResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundResultResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundSuccessResponseDto;
import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import com.nhnacademy.orderpaymentrefund.service.payment.impl.PaymentStrategyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundPolicyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@Import({SecurityConfig.class, HeaderFilter.class})
@WebMvcTest(RefundControllerImpl.class)
class RefundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefundService refundService;

    @MockBean
    private RefundPolicyService refundPolicyService;
    @MockBean
    private ClientHeaderContext clientHeaderContext;
    @MockBean
    private PaymentStrategyService paymentStrategyService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 취소")
    void paymentCancelTest() throws Exception {
        // Given
        PaymentCancelRequestDto requestDto = new PaymentCancelRequestDto();
        requestDto.setOrderId(1L);
        requestDto.setCancelReason("취소사유");
        requestDto.setOrderStatus("취소");

        PaymentMethodResponseDto paymentMethodResponseDto = PaymentMethodResponseDto.builder()
            .methodType("toss")
            .build();


        // Mocking
        when(refundService.findPayMethod(1L)).thenReturn(paymentMethodResponseDto);
        doNothing().when(paymentStrategyService).refundPayment(anyString(), anyLong(), anyString());
        doNothing().when(refundService).saveCancel(any(PaymentCancelRequestDto.class));

        // Convert requestDto to JSON
        String body = objectMapper.writeValueAsString(requestDto);

        // When & Then
        mockMvc.perform(
                post("/api/refund/cancel")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk());

        // Verify interactions
        verify(refundService).findPayMethod(1L);
        verify(paymentStrategyService).refundPayment("toss", 1L, "취소사유");
    }

    @Test
    @DisplayName("상품 반품 요청")
    void refundRequestTest() throws Exception {
        long orderId = 1L;
        RefundPolicyResponseDto responseDto = new RefundPolicyResponseDto(1L, "반품 정책");
        when(refundPolicyService.findRefundPolicy(anyLong())).thenReturn(List.of(responseDto));

        mockMvc.perform(
            get("/api/refund/request")
                .param("orderId", String.valueOf(orderId))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 반품 저장")
    void refundRequestSaveTest() throws Exception {
        RefundRequestDto requestDto = new RefundRequestDto();
        RefundSuccessResponseDto responseDto = new RefundSuccessResponseDto(10000L);

        when(refundService.saveRefund(any(RefundRequestDto.class))).thenReturn(responseDto);

        String body = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
            post("/api/refund/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 반품 요청 수락")
    void refundAccessTest() throws Exception {
        RefundAfterRequestDto requestDto = new RefundAfterRequestDto();
        requestDto.setOrderId(1L);

        RefundResultResponseDto resultResponseDto = new RefundResultResponseDto("반품사유","toss");
        when(refundService.refundUser(any(RefundAfterRequestDto.class))).thenReturn(resultResponseDto);
        doNothing().when(paymentStrategyService).refundPayment(anyString(), anyLong(), anyString());

        String body = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
            post("/api/refund/admin/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk());
    }
    @Test
    @DisplayName("환불 정책 저장")
    void saveRefundPolicyTest() throws Exception {
        // Given
        RefundPolicyRegisterRequestDto requestDto = RefundPolicyRegisterRequestDto.builder()
            .refundPolicyType("toss")
            .refundShippingFee(200).build();
        // set properties of requestDto here

        // Convert requestDto to JSON
        String body = objectMapper.writeValueAsString(requestDto);
        doNothing().when(refundPolicyService).saveRefundPolicy(requestDto);
        // When & Then
        mockMvc.perform(
                post("/api/refund/admin/policy/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isOk());

        // Verify interaction with service

    }

    @Test
    @DisplayName("환불 정책 리스트 조회")
    void findAllRefundPolicyTest() throws Exception {
        // Given
        int page = 0;
        int size = 10;
        RefundPolicyListResponseDto policy1 = RefundPolicyListResponseDto.builder().build();
        RefundPolicyListResponseDto policy2 =  RefundPolicyListResponseDto.builder().build();
        // set properties of policy1 and policy2 here

        Page<RefundPolicyListResponseDto> policyPage = new PageImpl<>(List.of(policy1, policy2));

        when(refundPolicyService.findPolicies(page, size)).thenReturn(policyPage);

        // When & Then
        mockMvc.perform(
                get("/api/refund/admin/policies")
                    .param("page", String.valueOf(page))
                    .param("size", String.valueOf(size))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());

        // Verify interaction with service
        verify(refundPolicyService).findPolicies(page, size);
    }
}
