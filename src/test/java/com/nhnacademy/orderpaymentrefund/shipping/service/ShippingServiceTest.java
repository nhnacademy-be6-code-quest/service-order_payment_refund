package com.nhnacademy.orderpaymentrefund.shipping.service;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.request.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.response.ShippingPolicyGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.ShippingPolicyNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.shipping.ShippingPolicyRepository;
import com.nhnacademy.orderpaymentrefund.service.shipping.impl.ShippingPolicyServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShippingServiceTest {

    @Mock
    private ShippingPolicyRepository shippingPolicyRepository;

    @InjectMocks
    private ShippingPolicyServiceImpl shippingPolicyService;

    private ShippingPolicy shippingPolicy;
    private ShippingPolicyType shippingPolicyType;

    @BeforeEach
    void setUp(){
        shippingPolicyType = ShippingPolicyType.CLIENT_SHIPPING;
        shippingPolicy = ShippingPolicy.builder()
                .description("테스트 배송비 정책")
                .shippingFee(3000)
                .minPurchaseAmount(30000)
                .shippingPolicyType(ShippingPolicyType.CLIENT_SHIPPING)
                .build();
    }

    @Test
    @DisplayName("배송 정책 단건 조회 - 성공")
    void getShippingPolicyTest(){

        when(shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType))
                .thenReturn(Optional.of(shippingPolicy));

        ShippingPolicyGetResponseDto response = shippingPolicyService.getShippingPolicy(shippingPolicyType);

        assertEquals(shippingPolicy.getShippingFee(), response.shippingFee());
        assertEquals(shippingPolicy.getDescription(), response.description());
        assertEquals(shippingPolicy.getMinPurchaseAmount(), response.minPurchaseAmount());

    }

    @Test
    @DisplayName("배송정책 단건 조회 실패 - 정책 없음")
    void getShippingPolicyNotFoundTest() {
        when(shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType))
                .thenReturn(Optional.empty());

        assertThrows(ShippingPolicyNotFoundException.class, () -> {
            shippingPolicyService.getShippingPolicy(shippingPolicyType);
        });
    }

    @Test
    @DisplayName("배송정책 수정")
    void updateShippingPolicyTest() {
        AdminShippingPolicyPutRequestDto requestDto = AdminShippingPolicyPutRequestDto.builder()
                .description("수정된 배송정책")
                .shippingFee(2000)
                .minPurchaseAmount(60000)
                .shippingPolicyType(shippingPolicyType)
                .build();

        // origin
        when(shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType))
                .thenReturn(Optional.of(shippingPolicy));

        // update
        shippingPolicyService.updateShippingPolicy(requestDto);

        // 검증
        verify(shippingPolicyRepository).findByShippingPolicyType(shippingPolicyType);
        verify(shippingPolicyRepository).save(shippingPolicy);

        // 변경된 값 검증
        assertEquals(requestDto.description(), shippingPolicy.getDescription());
        assertEquals(requestDto.shippingFee(), shippingPolicy.getShippingFee());
        assertEquals(requestDto.minPurchaseAmount(), shippingPolicy.getMinPurchaseAmount());

    }

    @Test
    @DisplayName("배송정책 수정 실패 - 정책 없음")
    void updateShippingPolicyFailTest() {
        AdminShippingPolicyPutRequestDto requestDto = AdminShippingPolicyPutRequestDto.builder()
                .description("수정된 배송정책")
                .shippingFee(2000)
                .minPurchaseAmount(60000)
                .shippingPolicyType(shippingPolicyType)
                .build();

        when(shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType))
                .thenReturn(Optional.empty());

        assertThrows(ShippingPolicyNotFoundException.class, () -> {
            shippingPolicyService.updateShippingPolicy(requestDto);
        });
    }

    @Test
    void testGetAllShippingPolicies() {
        // Given
        ShippingPolicy policy1 = ShippingPolicy.builder()
            .description("회원 배송 정책")
            .shippingFee(5000)
            .minPurchaseAmount(10000)
            .shippingPolicyType(ShippingPolicyType.CLIENT_SHIPPING)
            .build();

        ShippingPolicy policy2 = ShippingPolicy.builder()
            .description("비회원 배송 정책")
            .shippingFee(10000)
            .minPurchaseAmount(20000)
            .shippingPolicyType(ShippingPolicyType.NON_CLIENT_SHIPPING)
            .build();

        when(shippingPolicyRepository.findAll()).thenReturn(List.of(policy1, policy2));

        List<ShippingPolicyGetResponseDto> responseDtoList = shippingPolicyService.getAllShippingPolicies();

        assertNotNull(responseDtoList);
        assertEquals(2, responseDtoList.size());

        ShippingPolicyGetResponseDto dto1 = responseDtoList.getFirst();
        assertEquals("회원 배송 정책", dto1.description());
        assertEquals(5000, dto1.shippingFee());
        assertEquals(10000, dto1.minPurchaseAmount());
        assertEquals(ShippingPolicyType.CLIENT_SHIPPING, dto1.shippingPolicyType());

        ShippingPolicyGetResponseDto dto2 = responseDtoList.get(1);
        assertEquals("비회원 배송 정책", dto2.description());
        assertEquals(10000, dto2.shippingFee());
        assertEquals(20000, dto2.minPurchaseAmount());
        assertEquals(ShippingPolicyType.NON_CLIENT_SHIPPING, dto2.shippingPolicyType());

        verify(shippingPolicyRepository, times(1)).findAll();
    }
}
