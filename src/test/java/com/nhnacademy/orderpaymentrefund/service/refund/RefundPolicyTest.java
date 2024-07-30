package com.nhnacademy.orderpaymentrefund.service.refund;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.nhnacademy.orderpaymentrefund.domain.RefundPolicyStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundPolicyRegisterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyListResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundPolicyResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.RefundImpossibleException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundPolicyRepository;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class RefundPolicyTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RefundPolicyRepository refundPolicyRepository;

    @InjectMocks
    private RefundPolicyService refundPolicyService; // 수정: 서비스 클래스의 이름을 사용

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Order createOrder(Long orderId,
        LocalDate dateTime, OrderStatus orderStatus ) throws Exception {
        Constructor<Order> constructor = Order.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Order order = constructor.newInstance();
        setField(order, "orderId", orderId);
        setField(order, "deliveryStartDate",dateTime);
        setField(order, "orderStatus", orderStatus);

        return order;
    }
    private RefundPolicy createRefundPolicy() throws Exception {
        Constructor<RefundPolicy> constructor = RefundPolicy.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        RefundPolicy refundPolicy = constructor.newInstance();
        setField(refundPolicy, "refundPolicyId",1L);
        setField(refundPolicy, "refundPolicyType", "test");
        setField(refundPolicy, "refundShippingFee", 100);
        return refundPolicy;
    }


    @Test
    void testFindRefundPolicy_OrderNotFound() {
        long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(OrderNotFoundException.class, () -> {
            refundPolicyService.findRefundPolicy(orderId);
        });

        assertEquals("주문을 찾을 수 없습니다.", exception.getMessage());
    }



    @Test
     void testFindRefundPolicy_DeliveryStartDateWithin10Days() throws Exception {
        long orderId = 1L;

        // Arrange
        Order order = createOrder(1L, LocalDate.now().minusDays(5),OrderStatus.DELIVERING);

        RefundPolicy refundPolicy = createRefundPolicy();
        // Mock repository methods
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(refundPolicyRepository.findByRefundPolicyExpirationDateIsNull())
            .thenReturn(Collections.singletonList(refundPolicy));

        // Act
        List<RefundPolicyResponseDto> result = refundPolicyService.findRefundPolicy(orderId);

        // Assert
        assertEquals(1, result.size());
        RefundPolicyResponseDto dto = result.get(0);
        assertEquals(1L, dto.getRefundPolicyId());
        assertEquals("test", dto.getRefundPolicyType());
    }

     @Test
     void testFindPolicies() {
         RefundPolicy refundPolicy = RefundPolicy.builder()
             .refundPolicyType("Type1")
             .refundShippingFee(100)
             .refundPolicyIssuedDate(LocalDateTime.now())
             .refundPolicyStatus(RefundPolicyStatus.ACTIVATE)
             .build();
         Page<RefundPolicy> page = new PageImpl<>(Collections.singletonList(refundPolicy), PageRequest.of(0, 10, Sort.by("refundPolicyStatus").ascending()), 1);

         when(refundPolicyRepository.findAll(PageRequest.of(0, 10, Sort.by("refundPolicyStatus").ascending())))
             .thenReturn(page);

         Page<RefundPolicyListResponseDto> result = refundPolicyService.findPolicies(0, 10);

         assertEquals(1, result.getTotalElements(), "총 요소 수가 일치하지 않습니다.");
         RefundPolicyListResponseDto dto = result.getContent().getFirst();
         assertEquals("Type1", dto.getRefundPolicyType(), "환불 정책 유형이 일치하지 않습니다.");
         assertEquals(100, dto.getRefundShippingFee(), "환불 배송비가 일치하지 않습니다.");
         assertEquals(refundPolicy.getRefundPolicyIssuedDate().toString(), dto.getRefundPolicyIssuedDate(), "환불 정책 발행일이 일치하지 않습니다.");
         assertEquals(refundPolicy.getRefundPolicyStatus().getValue(), dto.getRefundPolicyStatus(), "환불 정책 상태가 일치하지 않습니다.");
     }

    @Test
    void testSaveRefundPolicy() {
        RefundPolicy existingPolicy = RefundPolicy.builder()
            .refundPolicyType("Type1")
            .refundShippingFee(100)
            .refundPolicyIssuedDate(LocalDateTime.now())
            .refundPolicyStatus(RefundPolicyStatus.ACTIVATE)
            .build();

        RefundPolicyRegisterRequestDto requestDto = RefundPolicyRegisterRequestDto.builder()
            .refundShippingFee(200)
            .refundPolicyType("Type1").build();


        when(refundPolicyRepository.findByRefundPolicyType("Type1"))
            .thenReturn(existingPolicy);

        refundPolicyService.saveRefundPolicy(requestDto);

        verify(refundPolicyRepository).findByRefundPolicyType("Type1");
        verify(refundPolicyRepository).save(existingPolicy);
        verify(refundPolicyRepository).save(argThat(newPolicy ->
            newPolicy.getRefundPolicyType().equals("Type1") &&
                newPolicy.getRefundShippingFee() == 200 &&
                newPolicy.getRefundPolicyStatus() == RefundPolicyStatus.ACTIVATE
        ));
    }
}
