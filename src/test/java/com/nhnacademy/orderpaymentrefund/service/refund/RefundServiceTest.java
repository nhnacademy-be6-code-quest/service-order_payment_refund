package com.nhnacademy.orderpaymentrefund.service.refund;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.orderpaymentrefund.dto.refund.response.TossPaymentRefundResponseDto;
import com.nhnacademy.orderpaymentrefund.service.payment.impl.PaymentServiceImpl;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
import com.nhnacademy.orderpaymentrefund.client.refund.TossPayRefundClient;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.Refund;
import com.nhnacademy.orderpaymentrefund.domain.refundandcancel.RefundPolicy;
import com.nhnacademy.orderpaymentrefund.dto.message.PointRewardRefundMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.message.PointUsageRefundMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.message.RefundCouponMessageDto;
import com.nhnacademy.orderpaymentrefund.dto.product.InventoryIncreaseRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.PaymentCancelRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundAfterRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.RefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.request.TossRefundRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundResultResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.refund.response.RefundSuccessResponseDto;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundPolicyRepository;
import com.nhnacademy.orderpaymentrefund.repository.refund.RefundRepository;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;

class RefundServiceTest {

    @InjectMocks
    private RefundService refundService;

    @Mock
    private RefundRepository refundRepository;

    @Mock
    private RefundPolicyRepository refundPolicyRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ProductOrderDetailRepository productOrderDetailRepository;

    @Mock
    private ProductOrderDetailOptionRepository productOrderDetailOptionRepository;

    @Mock
    private TossPayRefundClient tossPayRefundClient;


    @Value("${rabbit.refund.coupon.exchange.name}")
    private String refundCouponExchangeName;
    @Value("${rabbit.refund.coupon.routing.key}")
    private String refundCouponRoutingKey;

    @Value("${rabbit.refund.point.exchange.name}")
    private String refundPointExchangeName;
    @Value("${rabbit.refund.point.routing.key}")
    private String refundPointRoutingKey;

    @Value("${rabbit.usedRefund.point.exchange.name}")
    private String refundUsedPointExchangeName;
    @Value("${rabbit.usedRefund.point.routing.key}")
    private String refundUsedPointRoutingKey;

    @Value("${rabbit.inventory.increase.exchange.name}")
    private String increasesExchange;
    @Value("${rabbit.inventory.increase.routing.key}")
    private String increaseKey;

    private static final String TOSS_SECRET_KEY = "my-secret-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(refundService, "tossSecretKey", TOSS_SECRET_KEY);

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

    private ProductOrderDetail createProductOrderDetail(Long productId, Long quantity) throws Exception {
        Constructor<ProductOrderDetail> constructor = ProductOrderDetail.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ProductOrderDetail productOrderDetail = constructor.newInstance();
        setField(productOrderDetail, "productId",productId);
        setField(productOrderDetail, "quantity", quantity);
        return productOrderDetail;
    }

    private ProductOrderDetailOption createProductOrderDetailOption(Long productId, Long quantity) throws Exception {
        Constructor<ProductOrderDetailOption> constructor = ProductOrderDetailOption.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ProductOrderDetailOption productOrderDetailOption = constructor.newInstance();
        setField(productOrderDetailOption, "productId",productId);
        setField(productOrderDetailOption, "quantity", quantity);
        return productOrderDetailOption;
    }
    private Payment createPayment(String tossPaymentKey) {
        // Use builder to create Payment instance
        return Payment.builder()
            .order(null) // Set appropriate Order object if needed
            .payAmount(1000L) // Set appropriate payAmount
            .paymentMethodName("Card") // Set appropriate paymentMethodName
            .tossPaymentKey(tossPaymentKey)
            .build();
    }

    private Refund createRefund(Payment payment, String cancelReason)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // Use builder to create Payment instance
        Constructor<Refund> constructor = Refund.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Refund refund = constructor.newInstance();
        setField(refund, "payment",payment);
        setField(refund, "cancelReason", cancelReason);
        return refund;
    }






    @Test
    void testSaveRefund() throws Exception {
        Order order = createOrder(10000L, 500L, 200L, 300, 1L, OrderStatus.REFUND);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        Payment payment = new Payment();
        when(paymentRepository.findByOrder_OrderId(anyLong())).thenReturn(Optional.of(payment));

        RefundPolicy refundPolicy = new RefundPolicy();
        when(refundPolicyRepository.findById(anyLong())).thenReturn(Optional.of(refundPolicy));

        RefundRequestDto requestDto = new RefundRequestDto();
        requestDto.setOrderId(1L);
        requestDto.setRefundPolicyId(1L);
        requestDto.setRefundDetailReason("Test refund");

        RefundSuccessResponseDto response = refundService.saveRefund(requestDto);

        verify(orderRepository, times(1)).save(order);
        verify(refundRepository, times(1)).save(any(Refund.class));
        assert response.getRefundAmount() == 9000L;
    }

    @Test
    void testSaveCancel() throws Exception {
        // Arrange
        long orderId = 1L;
        Order order = createOrder(10000L, 500L, 200L, null, 1L, OrderStatus.PAYED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Mock repositories for product details
        List<ProductOrderDetail> productOrderDetails = new ArrayList<>();
        ProductOrderDetail productOrderDetail = createProductOrderDetail(1L, 10L);
        productOrderDetails.add(productOrderDetail);
        when(productOrderDetailRepository.findAllByOrder(order)).thenReturn(productOrderDetails);

        List<ProductOrderDetailOption> options = new ArrayList<>();
        ProductOrderDetailOption option = createProductOrderDetailOption(1L, 5L);
        options.add(option);
        when(productOrderDetailOptionRepository.findByProductOrderDetail(productOrderDetail)).thenReturn(options);

        PaymentCancelRequestDto requestDto = new PaymentCancelRequestDto();
        requestDto.setOrderId(orderId);
        requestDto.setOrderStatus(OrderStatus.PAYED.toString());

        // Act
        refundService.saveCancel(requestDto);

        // Assert
        verify(orderRepository, times(1)).save(order);

        // Verify messages sent to RabbitMQ
        verify(rabbitTemplate, times(1))
            .convertAndSend(eq(increasesExchange), eq(increaseKey), anyList());
        verify(rabbitTemplate, times(1))
            .convertAndSend(eq(refundCouponExchangeName), eq(refundCouponRoutingKey), any(RefundCouponMessageDto.class));
        verify(rabbitTemplate, times(1))
            .convertAndSend(eq(refundPointExchangeName), eq(refundPointRoutingKey), any(PointRewardRefundMessageDto.class));
        verify(rabbitTemplate, times(1))
            .convertAndSend(eq(refundUsedPointExchangeName), eq(refundUsedPointRoutingKey), any(PointUsageRefundMessageDto.class));
    }

    @Test
    void testTossRefund() {
        // Given
        long orderId = 123L;
        String cancelReason = "User requested refund";

        Payment payment = createPayment("mockPaymentKey");
        when(paymentRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.of(payment));

        // Prepare authorization header
        Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(TOSS_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String expectedAuthorization = "Basic " + new String(encodedBytes);

        TossRefundRequestDto expectedDto = TossRefundRequestDto.builder()
            .cancelReason(cancelReason)
            .build();

        // When
        refundService.tossRefund(orderId, cancelReason);

        // Then
        verify(tossPayRefundClient).cancelPayment(
            eq(payment.getTossPaymentKey()),
            argThat(dto -> dto.getCancelReason().equals(expectedDto.getCancelReason())),
            eq(expectedAuthorization)
        );
    }



//    @Test
//    void testRefundUser() throws Exception {
//        // Arrange
//        long orderId = 1L;
//        Order order = createOrder(10000L, 500L, 200L, null, 1L, OrderStatus.REFUND_REQUEST);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//
//        // Mock repositories for product details
//        List<ProductOrderDetail> productOrderDetails = new ArrayList<>();
//        ProductOrderDetail productOrderDetail = createProductOrderDetail(1L, 10L);
//        productOrderDetails.add(productOrderDetail);
//        when(productOrderDetailRepository.findAllByOrder(order)).thenReturn(productOrderDetails);
//
//        List<ProductOrderDetailOption> options = new ArrayList<>();
//        ProductOrderDetailOption option = createProductOrderDetailOption(1L, 5L);
//        options.add(option);
//        when(productOrderDetailOptionRepository.findByProductOrderDetail(productOrderDetail)).thenReturn(options);
//
//        Payment payment = createPayment("tossPaymentKey");
//        when(paymentRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.of(payment));
//
//        Refund refund = createRefund(payment, "cancel");
//        when(refundRepository.findByPayment(payment)).thenReturn(refund);
//
//        RefundAfterRequestDto requestDto = new RefundAfterRequestDto();
//        requestDto.setOrderId(orderId);
//
//        // Act
//        RefundResultResponseDto result = refundService.refundUser(requestDto); // Call the method under test
//
//        // Assert
//        assertNotNull(result);
//
//        verify(orderRepository, times(1)).save(any(Order.class)); // Verify save was called
//
//        // Verify messages sent to RabbitMQ
//        verify(rabbitTemplate, times(1))
//            .convertAndSend(eq(increasesExchange), eq(increaseKey), anyList());
//        verify(rabbitTemplate, times(1))
//            .convertAndSend(eq(refundCouponExchangeName), eq(refundCouponRoutingKey), any(RefundCouponMessageDto.class));
//        verify(rabbitTemplate, times(1))
//            .convertAndSend(eq(refundPointExchangeName), eq(refundPointRoutingKey), any(PointRewardRefundMessageDto.class));
//        verify(rabbitTemplate, times(1))
//            .convertAndSend(eq(refundUsedPointExchangeName), eq(refundUsedPointRoutingKey), any(PointUsageRefundMessageDto.class));
//    }
}
