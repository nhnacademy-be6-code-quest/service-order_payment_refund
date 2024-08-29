//package com.nhnacademy.orderpaymentrefund.service.payment;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.orderpaymentrefund.client.payment.TossPaymentsClient;
//import com.nhnacademy.orderpaymentrefund.client.refund.TossPayRefundClient;
//import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
//import com.nhnacademy.orderpaymentrefund.domain.order.Order;
//import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
//import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
//import com.nhnacademy.orderpaymentrefund.domain.payment.PaymentMethodType;
//import com.nhnacademy.orderpaymentrefund.dto.payment.request.ApprovePaymentRequestDto;
//import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentsResponseDto;
//import com.nhnacademy.orderpaymentrefund.dto.refund.request.TossRefundRequestDto;
//import com.nhnacademy.orderpaymentrefund.exception.PaymentNotFoundException;
//import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
//import com.nhnacademy.orderpaymentrefund.service.payment.impl.TossPGServiceStrategy;
//import java.lang.reflect.Constructor;
//import org.json.simple.parser.ParseException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.util.ReflectionTestUtils.setField;
//
//class TossPGServiceStrategyTest {
//
//    @Mock
//    private ClientHeaderContext clientHeaderContext;
//
//    @Mock
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Mock
//    private HashOperations<String, Object, Object> hashOperations;
//
//    @Mock
//    private ObjectMapper objectMapper;
//
//    @Mock
//    private TossPaymentsClient tossPaymentsClient;
//
//    @Mock
//    private PaymentRepository paymentRepository;
//
//    @Mock
//    private TossPayRefundClient tossPayRefundClient;
//
//    @InjectMocks
//    private TossPGServiceStrategy tossPaymentStrategy;
//
//    private String tossSecretKey = "testSecretKey";
//    private Order createOrder(Long orderId, Long orderTotalAmount, Integer shippingFee, OrderStatus orderStatus ) throws Exception {
//        Constructor<Order> constructor = Order.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        Order order = constructor.newInstance();
//        setField(order, "orderId", orderId);
//        setField(order, "orderTotalAmount", orderTotalAmount);
//        setField(order, "shippingFee", shippingFee);
//        setField(order, "orderStatus", orderStatus);
//        return order;
//    }
//    private PaymentMethodType createPaymentMethodType( ) throws Exception {
//        Constructor<PaymentMethodType> constructor = PaymentMethodType.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        PaymentMethodType paymentMethodType = constructor.newInstance();
//        setField(paymentMethodType, "paymentMethodTypeId", 1L);
//        setField(paymentMethodType, "paymentMethodTypeName", "toss");
//        return paymentMethodType;
//    }
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        tossPaymentStrategy = new TossPGServiceStrategy(new PGServiceUtil(), clientHeaderContext, redisTemplate, objectMapper, tossSecretKey, tossPaymentsClient, paymentRepository, tossPayRefundClient);
//    }
//
//    @Test
//    void approvePayment_Success() throws ParseException {
//        // Arrange
//        ApprovePaymentRequestDto requestDto = new ApprovePaymentRequestDto("orderId123",  1000L,"paymentKey123","toss");
//        String responseJson = "{\"orderName\":\"Test Order\",\"totalAmount\":\"1000\",\"method\":\"카드\",\"card\":{\"number\":\"1234-5678-9101-1121\"}}";
//        when(tossPaymentsClient.approvePayment(any(), any())).thenReturn(responseJson);
//
//        // Act
//        PaymentsResponseDto responseDto = tossPaymentStrategy.approvePayment(requestDto);
//
//        // Assert
//        assertNotNull(responseDto);
//        assertEquals("Test Order", responseDto.getOrderName());
//        assertEquals(1000L, responseDto.getTotalAmount());
//        assertEquals("카드", responseDto.getMethod());
//        assertEquals("1234-5678-9101-1121", responseDto.getCardNumber());
//        assertNull(responseDto.getAccountNumber());
//        assertNull(responseDto.getBank());
//        assertNull(responseDto.getCustomerMobilePhone());
//        assertEquals("paymentKey123", responseDto.getPaymentKey());
//        assertEquals("orderId123", responseDto.getOrderCode());
//    }
//
//    @Test
//    void refundPayment_Success() throws Exception {
//        // Arrange
//        Order order = createOrder(1L,5000L, 200,OrderStatus.WAIT_PAYMENT);
//        PaymentMethodType paymentMethodType = createPaymentMethodType();
//        Payment payment = new Payment(order, 5000L, "toss", paymentMethodType, "paymentKey123");
//        when(paymentRepository.findByOrder_OrderId(anyLong())).thenReturn(Optional.of(payment));
//
//        // Act
//        tossPaymentStrategy.refundPayment(12345L, "Refund reason");
//
//        // Assert
//        verify(tossPayRefundClient).cancelPayment(eq("paymentKey123"), any(TossRefundRequestDto.class), anyString());
//    }
//
//    @Test
//    void refundPayment_PaymentNotFound() {
//        // Arrange
//        when(paymentRepository.findByOrder_OrderId(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(PaymentNotFoundException.class, () -> tossPaymentStrategy.refundPayment(12345L, "Refund reason"));
//    }
//}
