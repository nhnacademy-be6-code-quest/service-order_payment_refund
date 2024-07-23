package com.nhnacademy.orderpaymentrefund.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.client.client.ClientServiceFeignClient;
import com.nhnacademy.orderpaymentrefund.client.payment.TossPaymentsClient;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.dto.client.ClientUpdateGradeRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.coupon.PaymentCompletedCouponResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.message.PointUsagePaymentMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.message.PointUsageRefundMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.TossApprovePaymentRequest;
import com.nhnacademy.orderpaymentrefund.dto.payment.request.UserUpdateGradeRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.TossPaymentsResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import com.nhnacademy.orderpaymentrefund.dto.product.CartCheckoutRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.product.InventoryDecreaseRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.service.payment.impl.PaymentServiceImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class PaymentServiceImplTest {

    @Value("${rabbit.cart.checkout.exchange.name}")
    private String cartCheckoutExchangeName;
    @Value("${rabbit.cart.checkout.routing.key}")
    private String cartCheckoutRoutingKey;

    @Value("${rabbit.inventory.decrease.exchange.name}")
    private String inventoryDecreaseExchangeName;
    @Value("${rabbit.inventory.decrease.routing.key}")
    private String inventoryDecreaseRoutingKey;

    @Value("${rabbit.use.point.exchange.name}")
    private String pointUseExchangeName;
    @Value("${rabbit.use.point.routing.key}")
    private String pointUseRoutingKey;

    @Value("${rabbit.use.coupon.exchange.name}")
    private String couponUseExchangeName;
    @Value("${rabbit.use.coupon.roting.key}")
    private String couponUseRoutingKey;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductOrderDetailRepository productOrderDetailRepository;
    @Mock
    private ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private TossPaymentsClient tossPaymentsClient;
    @Mock
    private ClientServiceFeignClient clientServiceFeignClient;
    @Mock
    private RabbitTemplate rabbitTemplate;

    private String tossSecretKey;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        HashOperations<String, Object, Object> hashOps = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOps);
        tossSecretKey = "test-secret-key"; // Set the value directly정
        ReflectionTestUtils.setField(paymentService, "tossSecretKey", tossSecretKey);

    }
    private Order createOrder(Long orderTotalAmount, Long discountAmountByPoint, Long discountAmountByCoupon, Integer shippingFee, Long couponId, OrderStatus orderStatus ) throws Exception {
        Constructor<Order> constructor = Order.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Order order = constructor.newInstance();
        setField(order, "orderTotalAmount", orderTotalAmount);
        setField(order, "discountAmountByPoint", discountAmountByPoint);
        setField(order, "discountAmountByCoupon", discountAmountByCoupon);
        setField(order, "shippingFee", shippingFee);
        setField(order, "couponId", couponId);
        setField(order, "orderStatus", orderStatus);
        return order;
    }
    private ClientOrderCreateForm createClientOrderCreateForm(String tossOrderId, List<OrderDetailDtoItem> orderDetailDtoItemList) throws Exception {
        Constructor<ClientOrderCreateForm> constructor = ClientOrderCreateForm.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ClientOrderCreateForm form = constructor.newInstance();
        setField(form, "tossOrderId", tossOrderId); // Set the tossOrderId
        setField(form, "orderDetailDtoItemList", orderDetailDtoItemList); // Set the orderDetailDtoItemList
        setField(form, "productTotalAmount", 1000L); // Example value, replace as needed
        setField(form, "orderTotalAmount", 1050L); // Example value, replace as needed
        setField(form, "payAmount", 1000L); // Example value, replace as needed
        setField(form, "shippingFee", 50); // Example value, replace as needed
        setField(form, "couponId", 1L); // Example value, replace as needed
        setField(form, "couponDiscountAmount", 50L); // Example value, replace as needed
        setField(form, "usedPointDiscountAmount", 50L); // Example value, replace as needed
        setField(form, "orderedPersonName", "John Doe"); // Example value, replace as needed
        setField(form, "phoneNumber", "1234567890"); // Example value, replace as needed
        setField(form, "deliveryAddress", "123 Main St"); // Example value, replace as needed
        setField(form, "useDesignatedDeliveryDate", true); // Example value, replace as needed
        setField(form, "designatedDeliveryDate", LocalDate.now()); // Example value, replace as needed
        setField(form, "paymentMethod", 1); // Example value, replace as needed
        setField(form, "accumulatePoint", 100L); // Example value, replace as needed

        return form;
    }

    private NonClientOrderForm createNonClientOrderForm(String tossOrderId, List<OrderDetailDtoItem> orderDetailDtoItemList) throws Exception {
        Constructor<NonClientOrderForm> constructor = NonClientOrderForm.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        NonClientOrderForm form = constructor.newInstance();
        setField(form, "tossOrderId", tossOrderId); // Set the tossOrderId
        setField(form, "orderDetailDtoItemList", orderDetailDtoItemList); // Set the orderDetailDtoItemList
        setField(form, "productTotalAmount", 1000L); // Example value, replace as needed
        setField(form, "payAmount", 1000L); // Example value, replace as needed
        setField(form, "shippingFee", 50); // Example value, replace as needed
        setField(form, "orderedPersonName", "John Doe"); // Example value, replace as needed
        setField(form, "phoneNumber", "1234567890"); // Example value, replace as needed
        setField(form, "deliveryAddress", "123 Main St"); // Example value, replace as needed
        setField(form, "useDesignatedDeliveryDate", true); // Example value, replace as needed
        setField(form, "designatedDeliveryDate", LocalDate.now()); // Example value, replace as needed
        setField(form, "paymentMethod", 1); // Example value, replace as needed

        return form;
    }

    private void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Make private fields accessible
        field.set(target, value);
    }

    @Test
    void testGetPaymentRecordOfClient() {
        // Given
        Long clientId = 1L;
        Long totalOptionPriceForLastThreeMonth = 500L;
        Long sumFinalAmountForCompletedOrders = 1200L;

        // Mock behaviors
        when(orderRepository.getTotalOptionPriceForLastThreeMonths(eq(clientId), any(LocalDateTime.class)))
            .thenReturn(totalOptionPriceForLastThreeMonth);
        when(orderRepository.sumFinalAmountForCompletedOrders(eq(clientId), any(LocalDateTime.class)))
            .thenReturn(sumFinalAmountForCompletedOrders);

        // When
        PaymentGradeResponseDto responseDto = paymentService.getPaymentRecordOfClient(clientId);

        // Then
        Long expectedPaymentGradeValue = sumFinalAmountForCompletedOrders - totalOptionPriceForLastThreeMonth;
        assertEquals(expectedPaymentGradeValue, responseDto.getPaymentGradeValue());
    }

    @Test
    void testGetPaymentRecordOfClient_withNullValues() {
        // Given
        Long clientId = 1L;

        // Mock behaviors
        when(orderRepository.getTotalOptionPriceForLastThreeMonths(eq(clientId), any(LocalDateTime.class)))
            .thenReturn(null);
        when(orderRepository.sumFinalAmountForCompletedOrders(eq(clientId), any(LocalDateTime.class)))
            .thenReturn(null);

        // When
        PaymentGradeResponseDto responseDto = paymentService.getPaymentRecordOfClient(clientId);

        // Then
        assertEquals(0L, responseDto.getPaymentGradeValue());
    }
    @Test
    void testApprovePayment_withCardMethod() throws Exception {
        // Given
        TossApprovePaymentRequest request = new TossApprovePaymentRequest ("test-order-id", 5000, "test-payment-key");

        String mockResponse = "{"
            + "\"orderName\":\"Order1\","
            + "\"totalAmount\":\"1000\","
            + "\"method\":\"카드\","
            + "\"card\":{\"number\":\"1234-5678-9876-5432\"}"
            + "}";

        // Mock 동작 설정
        when(tossPaymentsClient.approvePayment(any(TossApprovePaymentRequest.class), anyString()))
            .thenReturn(mockResponse);

        // 테스트할 메소드 호출
        TossPaymentsResponseDto response = paymentService.approvePayment(request);

        // 결과 검증
        assertNotNull(response);
        assertEquals("Order1", response.getOrderName());
        assertEquals(1000L, response.getTotalAmount());
        assertEquals("카드", response.getMethod());
        assertEquals("1234-5678-9876-5432", response.getCardNumber());
        assertNull(response.getAccountNumber());
        assertNull(response.getBank());
        assertNull(response.getCustomerMobilePhone());
        assertEquals("test-payment-key", response.getPaymentKey());
        assertEquals("test-order-id", response.getOrderId());
    }

}
