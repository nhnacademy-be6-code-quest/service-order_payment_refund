package com.nhnacademy.orderpaymentrefund.controller.refund;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRequestDto;
import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import com.nhnacademy.orderpaymentrefund.service.refund.RefundPolicyService;
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
@WebMvcTest(RefundPolicyControllerImpl.class)
class RefundPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefundPolicyService refundPolicyService;

    @Test
    @DisplayName("모든 환불 정책 조회")
    void findAllRefundPolicyRequestDtoListTest() throws Exception {

        RefundPolicyRequestDto refundPolicyRequestDto1 = RefundPolicyRequestDto.builder()
            .refundAndCancelPolicyReason("Reason 1")
            .refundAndCancelPolicyType("Type 1")
            .refundShippingFee(1000)
            .build();

        RefundPolicyRequestDto refundPolicyRequestDto2 = RefundPolicyRequestDto.builder()
            .refundAndCancelPolicyReason("Reason 2")
            .refundAndCancelPolicyType("Type 2")
            .refundShippingFee(2000)
            .build();

        List<RefundPolicyRequestDto> refundPolicyList = List.of(refundPolicyRequestDto1, refundPolicyRequestDto2);

        when(refundPolicyService.findAllRefundPolicyRequestDtoList()).thenReturn(refundPolicyList);

        mockMvc.perform(get("/api/refund/refund-policy")
                .contentType(MediaType.APPLICATION_JSON));

        verify(refundPolicyService, times(1)).findAllRefundPolicyRequestDtoList();
    }
}
