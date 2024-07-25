package com.nhnacademy.orderpaymentrefund.controller.shipping;

import static com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType.CLIENT_SHIPPING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.config.SecurityConfig;
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.request.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.response.ShippingPolicyGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.ShippingPolicyNotFoundException;
import com.nhnacademy.orderpaymentrefund.filter.HeaderFilter;
import com.nhnacademy.orderpaymentrefund.service.shipping.ShippingPolicyService;
import java.util.ArrayList;
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
@WebMvcTest(ShippingController.class)
class ShippingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ShippingPolicyService shippingPolicyService;

    @MockBean
    ClientHeaderContext clientHeaderContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("배송비 정책 수정 성공")
    void updateShippingPolicySuccessTest() throws Exception {

        AdminShippingPolicyPutRequestDto requestDto = AdminShippingPolicyPutRequestDto.builder()
            .description("수정된 배송비 정책입니다")
            .shippingFee(2500)
            .minPurchaseAmount(30000)
            .shippingPolicyType(CLIENT_SHIPPING)
            .build();

        String body = objectMapper.writeValueAsString(requestDto);

        doNothing().when(shippingPolicyService)
            .updateShippingPolicy(any(AdminShippingPolicyPutRequestDto.class));

        mockMvc.perform(
            put("/api/shipping-policy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("배송비 정책 수정 실패")
    void updateShippingPolicyFailTest() throws Exception {

        AdminShippingPolicyPutRequestDto requestDto = AdminShippingPolicyPutRequestDto.builder()
            .description("수정된 배송비 정책입니다")
            .shippingFee(2500)
            .minPurchaseAmount(30000)
            .shippingPolicyType(CLIENT_SHIPPING)
            .build();

        String body = objectMapper.writeValueAsString(requestDto);

        doThrow(ShippingPolicyNotFoundException.class).when(shippingPolicyService)
            .updateShippingPolicy(any(AdminShippingPolicyPutRequestDto.class));

        mockMvc.perform(
            put("/api/shipping-policy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("배송비 정책 단건 조회")
    void getShippingPolicyTest() throws Exception {

        String type = "비회원배송비";

        ShippingPolicyGetResponseDto responseDto = ShippingPolicyGetResponseDto.builder()
            .description("조회한 비회원 배송비입니다.")
            .shippingFee(5000)
            .minPurchaseAmount(50000)
            .shippingPolicyType(ShippingPolicyType.NON_CLIENT_SHIPPING)
            .build();

        when(shippingPolicyService.getShippingPolicy(any(ShippingPolicyType.class))).thenReturn(responseDto);

        mockMvc.perform(
            get("/api/shipping-policy")
                .param("type", type)
        ).andExpect(status().isOk());

    }

    @Test
    @DisplayName("배송비 정책 전체 조회")
    void getAllShippingPoliciesTest() throws Exception {

        List<ShippingPolicyGetResponseDto> responseDto = new ArrayList<>();
        responseDto.add(
            ShippingPolicyGetResponseDto.builder()
                .description("조회한 배송비 정책입니다.")
                .shippingFee(2500)
                .minPurchaseAmount(30000)
                .shippingPolicyType(CLIENT_SHIPPING)
                .build()
        );

        when(shippingPolicyService.getAllShippingPolicies()).thenReturn(responseDto);

        mockMvc.perform(
            get("/api/shipping-policy/all")
        ).andExpect(status().isOk());

    }

}
