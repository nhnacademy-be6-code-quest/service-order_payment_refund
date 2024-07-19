package com.nhnacademy.orderpaymentrefund.shipping.service;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.request.AdminShippingPolicyPutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.shipping.admin.response.ShippingPolicyGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.ShippingPolicyNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.shipping.ShippingPolicyRepository;
import com.nhnacademy.orderpaymentrefund.service.shipping.impl.ShippingPolicyServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ShippingServiceTest {

    @Mock
    private ShippingPolicyRepository shippingPolicyRepository;

    @InjectMocks
    private ShippingPolicyServiceImpl shippingPolicyService;

    private ShippingPolicy shippingPolicy;
    private ShippingPolicyType shippingPolicyType;
    private ShippingPolicyGetResponseDto shippingPolicyGetResponseDto;

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

        Mockito.when(shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType))
                .thenReturn(Optional.of(shippingPolicy));

        ShippingPolicyGetResponseDto response = shippingPolicyService.getShippingPolicy(shippingPolicyType);

        assertEquals(shippingPolicy.getShippingFee(), response.shippingFee());
        assertEquals(shippingPolicy.getDescription(), response.description());
        assertEquals(shippingPolicy.getMinPurchaseAmount(), response.minPurchaseAmount());
        assertEquals(shippingPolicy.getShippingPolicyType(), response.shippingPolicyType());

    }

    @Test
    @DisplayName("배송정책 단건 조회 실패 - 정책 없음")
    void getShippingPolicyNotFoundTest() {
        Mockito.when(shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType))
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
        Mockito.when(shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType))
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

        // origin
        Mockito.when(shippingPolicyRepository.findByShippingPolicyType(shippingPolicyType))
                .thenReturn(Optional.empty());

        assertThrows(ShippingPolicyNotFoundException.class, () -> {
            shippingPolicyService.updateShippingPolicy(requestDto);
        });
    }
}
