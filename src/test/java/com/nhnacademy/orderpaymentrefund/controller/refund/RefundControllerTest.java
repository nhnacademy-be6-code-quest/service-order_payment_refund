package com.nhnacademy.orderpaymentrefund.controller.refund;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundAfterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundResultResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundSuccessResponseDto;
import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundPolicyService;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 취소")
    void paymentCancelTest() throws Exception {
        PaymentCancelRequestDto requestDto = new PaymentCancelRequestDto();
        requestDto.setOrderId(1L);
        requestDto.setCancelReason("취소사유");
        requestDto.setOrderStatus("취소");

        String body = objectMapper.writeValueAsString(requestDto);

        doNothing().when(refundService).tossRefund(anyLong(), anyString());
        doNothing().when(refundService).saveCancel(any(PaymentCancelRequestDto.class));

        mockMvc.perform(
            post("/api/refund/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 반품 요청")
    void refundRequestTest() throws Exception {
        long orderId = 1L;
        RefundPolicyResponseDto responseDto = new RefundPolicyResponseDto(1L, "반품 정책", 5000);
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

        RefundResultResponseDto resultResponseDto = new RefundResultResponseDto("반품사유");
        when(refundService.refundUser(any(RefundAfterRequestDto.class))).thenReturn(resultResponseDto);
        doNothing().when(refundService).tossRefund(anyLong(), anyString());

        String body = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
            post("/api/refund/admin/refund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk());
    }
}
