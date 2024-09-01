//package com.nhnacademy.orderpaymentrefund.service.payment;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nhnacademy.orderpaymentrefund.client.payment.TossPaymentsClient;
//import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverter;
//import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
//import com.nhnacademy.orderpaymentrefund.domain.order.ClientOrder;
//import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
//import com.nhnacademy.orderpaymentrefund.domain.order.Order;
//import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
//import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
//import com.nhnacademy.orderpaymentrefund.domain.payment.Payment;
//import com.nhnacademy.orderpaymentrefund.domain.payment.PaymentMethodType;
//import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderForm;
//import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
//import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
//import com.nhnacademy.orderpaymentrefund.dto.payment.response.PaymentGradeResponseDto;
//import com.nhnacademy.orderpaymentrefund.dto.payment.response.ApprovePaymentResponseDto;
//import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
//import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
//import com.nhnacademy.orderpaymentrefund.repository.order.NonClientOrderRepository;
//import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
//import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
//import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentMethodTypeRepository;
//import com.nhnacademy.orderpaymentrefund.repository.payment.PaymentRepository;
//import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
//import com.nhnacademy.orderpaymentrefund.service.payment.impl.PaymentServiceImpl;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import java.util.Objects;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpHeaders;
//import org.springframework.test.util.ReflectionTestUtils;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentServiceTest {
//
//    @Value("${rabbit.cart.checkout.exchange.name}")
//    private String cartCheckoutExchangeName;
//    @Value("${rabbit.cart.checkout.routing.key}")
//    private String cartCheckoutRoutingKey;
//
//    @Value("${rabbit.inventory.decrease.exchange.name}")
//    private String inventoryDecreaseExchangeName;
//    @Value("${rabbit.inventory.decrease.routing.key}")
//    private String inventoryDecreaseRoutingKey;
//
//    @Value("${rabbit.use.point.exchange.name}")
//    private String pointUseExchangeName;
//    @Value("${rabbit.use.point.routing.key}")
//    private String pointUseRoutingKey;
//
//    @Value("${rabbit.use.coupon.exchange.name}")
//    private String couponUseExchangeName;
//    @Value("${rabbit.use.coupon.roting.key}")
//    private String couponUseRoutingKey;
//
//    @Mock
//    ClientOrderRepository clientOrderRepository;
//
//    @Mock
//    private ProductOrderDetailOptionConverter productOrderDetailOptionConverter;
//    @Mock
//    private RedisTemplate<String, Object> redisTemplate;
//    @Mock
//    private ObjectMapper objectMapper;
//    @Mock
//    private TossPaymentsClient tossPaymentsClient;
//    @Mock
//    private RabbitTemplate rabbitTemplate;
//    @Mock
//    private OrderService orderService;
//
//    @Mock
//    private HashOperations<String, Object, Object> hashOperations;
//
//    @Mock
//    private ProductOrderDetailConverter productOrderDetailConverter;
//
//    @Mock
//    private OrderRepository orderRepository;
//    @Mock
//    private PaymentMethodTypeRepository paymentMethodTypeRepository;
//    @Mock
//    private PaymentRepository paymentRepository;
//    @Mock
//    private NonClientOrderRepository nonClientOrderRepository;
//    @Mock
//    private ProductOrderDetailRepository productOrderDetailRepository;
//    private Long clientId;
//    private Object data;
//    private ApprovePaymentResponseDto approvePaymentResponseDto;
//    private ClientOrderForm clientOrderForm;
//    private NonClientOrderForm nonClientOrderForm;
//    private HttpHeaders headers= new HttpHeaders();
//    List<OrderDetailDtoItem> detailDtoItems = new ArrayList<>();
//    @InjectMocks
//    private PaymentServiceImpl paymentService;
//    private static final String ID_HEADER = "Client-ID";
//
//    @BeforeEach
//    void setUp() throws Exception {
//        //MockitoAnnotations.openMocks(this);
//
//
//    }
//
//
//
//    private Long getClientId(HttpHeaders headers) {
//        if (headers.get(ID_HEADER) == null) {
//            return null;
//        }
//        return Long.parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
//    }
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
//
//
//
//    private OrderDetailDtoItem createOrderDetailDtoItem(Boolean usePackaging)
//        throws Exception {
//        Constructor<OrderDetailDtoItem> constructor = OrderDetailDtoItem.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        OrderDetailDtoItem orderDetailDtoItem = constructor.newInstance();
//        setField(orderDetailDtoItem, "usePackaging", usePackaging);
//        return orderDetailDtoItem;
//    }
//
//    private ClientOrder createClientOrder( Long discountAmountByPoint, Long discountAmountByCoupon, OrderStatus orderStatus)
//        throws Exception {
//        Constructor<ClientOrder> constructor = ClientOrder.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        ClientOrder clientOrder = constructor.newInstance();
//        setField(clientOrder, "discountAmountByPoint", discountAmountByPoint);
//        setField(clientOrder, "discountAmountByCoupon", discountAmountByCoupon);
//        setField(clientOrder, "orderStatus", orderStatus);
//        return clientOrder;
//
//    }
//
//    private ClientOrderForm createClientOrderCreateForm(List<OrderDetailDtoItem> orderDetailDtoItemList) throws Exception {
//        Constructor<ClientOrderForm> constructor = ClientOrderForm.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        ClientOrderForm form = constructor.newInstance();
//        setField(form, "orderCode", "orderCode");
//        setField(form, "orderDetailDtoItemList", orderDetailDtoItemList);
//        setField(form, "productTotalAmount", 1000L);
//        setField(form, "orderTotalAmount", 1050L);
//        setField(form, "payAmount", 1000L);
//        setField(form, "shippingFee", 50);
//        setField(form, "couponId", 1L);
//        setField(form, "couponDiscountAmount", 50L);
//        setField(form, "usedPointDiscountAmount", 50L);
//        setField(form, "phoneNumber", "1234567890");
//        setField(form, "deliveryAddress", "123 Main St");
//        setField(form, "useDesignatedDeliveryDate", true);
//        setField(form, "designatedDeliveryDate", LocalDate.now());
//        setField(form, "paymentMethod", "1");
//        setField(form, "accumulatePoint", 100L);
//
//        return form;
//    }
//    private PaymentMethodType createPaymentMethodType() throws Exception {
//        Constructor<PaymentMethodType> constructor = PaymentMethodType.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        PaymentMethodType form = constructor.newInstance();
//        setField(form, "paymentMethodTypeName", "toss"); // Set the orderCode
//        return form;
//    }
//
//    private NonClientOrderForm createNonClientOrderForm(String orderCode, List<OrderDetailDtoItem> orderDetailDtoItemList) throws Exception {
//        Constructor<NonClientOrderForm> constructor = NonClientOrderForm.class.getDeclaredConstructor();
//        constructor.setAccessible(true);
//        NonClientOrderForm form = constructor.newInstance();
//        setField(form, "orderCode", orderCode); // Set the orderCode
//        setField(form, "orderDetailDtoItemList", orderDetailDtoItemList); // Set the orderDetailDtoItemList
//        setField(form, "productTotalAmount", 1000L); // Example value, replace as needed
//        setField(form, "payAmount", 1000L); // Example value, replace as needed
//        setField(form, "shippingFee", 50); // Example value, replace as needed
//        setField(form, "orderedPersonName", "John Doe"); // Example value, replace as needed
//        setField(form, "phoneNumber", "1234567890"); // Example value, replace as needed
//        setField(form, "deliveryAddress", "123 Main St"); // Example value, replace as needed
//        setField(form, "useDesignatedDeliveryDate", true); // Example value, replace as needed
//        setField(form, "designatedDeliveryDate", LocalDate.now()); // Example value, replace as needed
//        setField(form, "paymentMethod", "toss");
//        setField(form, "orderPassword", "toss");
//        setField(form, "email", "jms@naver.com");
//        setField(form, "addressZipCode", "214124421");
//
//
//        return form;
//    }
//
//    private void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
//        Field field = target.getClass().getDeclaredField(fieldName);
//        field.setAccessible(true);
//        field.set(target, value);
//    }
//
//    @Test
//    void testSavePaymentWithClientId() throws Exception {
//        // Arrange
//        given(redisTemplate.opsForHash()).willReturn(hashOperations);
//
//        clientId = 1L;
//        data = new Object(); // 테스트용 데이터
//        this.approvePaymentResponseDto = new ApprovePaymentResponseDto(); // 필요한 필드 초기화
//        clientOrderForm = createClientOrderCreateForm(detailDtoItems);// 필요한 필드 초기화
//        headers = new HttpHeaders(); // HttpHeaders 초기화
//        nonClientOrderForm = createNonClientOrderForm("orderCode",detailDtoItems);
//        headers.set("X-User-Id","1");
//
//        ApprovePaymentResponseDto approvePaymentResponseDto = new ApprovePaymentResponseDto();
//        ReflectionTestUtils.setField(approvePaymentResponseDto, "orderCode", "order123");
//        ReflectionTestUtils.setField(approvePaymentResponseDto, "totalAmount", 10000L);
//        ReflectionTestUtils.setField(approvePaymentResponseDto, "paymentKey","paymentKey");
//        ReflectionTestUtils.setField(approvePaymentResponseDto, "methodType", "toss");
//        PaymentMethodType paymentMethodType = createPaymentMethodType();
//        Object data = clientOrderForm; // Replace with actual type
//
//        // Mock the methods
//        when(hashOperations.get(anyString(), anyString())).thenReturn(data);
//        when(objectMapper.convertValue(data, ClientOrderForm.class)).thenReturn(clientOrderForm);
//        when(paymentMethodTypeRepository.findByPaymentMethodTypeNameEquals(approvePaymentResponseDto.getMethodType())).thenReturn(paymentMethodType);
//        // Act
//        paymentService.savePayment(headers, approvePaymentResponseDto);
//
//        // Assert
//        // Verify interactions with mocks and state changes
//        verify(hashOperations).delete("order", "orderCode");
//        // Add more assertions to check the proper execution of private logic
//    }
//
//    @Test
//    void testSavePaymentWithNonClientId() throws Exception {
//        // Arrange
//        given(redisTemplate.opsForHash()).willReturn(hashOperations);
//
//        clientId = 1L;
//        data = new Object(); // 테스트용 데이터
//        this.approvePaymentResponseDto = new ApprovePaymentResponseDto(); // 필요한 필드 초기화
//        clientOrderForm = createClientOrderCreateForm(detailDtoItems);// 필요한 필드 초기화
//        headers = new HttpHeaders(); // HttpHeaders 초기화
//        nonClientOrderForm = createNonClientOrderForm("orderCode",detailDtoItems);
//
//        ApprovePaymentResponseDto approvePaymentResponseDto = new ApprovePaymentResponseDto();
//        ReflectionTestUtils.setField(approvePaymentResponseDto, "orderCode", "order123");
//        ReflectionTestUtils.setField(approvePaymentResponseDto, "totalAmount", 10000L);
//        ReflectionTestUtils.setField(approvePaymentResponseDto, "paymentKey","paymentKey");
//        ReflectionTestUtils.setField(approvePaymentResponseDto, "methodType", "toss");
//
//        PaymentMethodType paymentMethodType = createPaymentMethodType();
//        Object data =nonClientOrderForm;
//        NonClientOrder nonClientOrder = NonClientOrder.builder()
//            .nonClientOrdererEmail(nonClientOrderForm.getEmail())
//            .nonClientOrdererName(nonClientOrderForm.getOrderedPersonName())
//            .nonClientOrderPassword(nonClientOrderForm.getOrderPassword())
//            .build();
//        // Mock the methods
//        when(hashOperations.get(anyString(), anyString())).thenReturn(data);
//        when(objectMapper.convertValue(data, NonClientOrderForm.class)).thenReturn(nonClientOrderForm);
//        when(paymentMethodTypeRepository.findByPaymentMethodTypeNameEquals(approvePaymentResponseDto.getMethodType())).thenReturn(paymentMethodType);
//        when(nonClientOrderRepository.save(any())).thenReturn(nonClientOrder);
//
//        // Act
//        paymentService.savePayment(headers, approvePaymentResponseDto);
//
//        // Assert
//        // Verify interactions with mocks and state changes
//        verify(hashOperations).delete("order", "orderCode");
//        // Add more assertions to check the proper execution of private logic
//    }
//
//    @Test
//    void testGetPaymentRecordOfClient() {
//        Long clientId = 1L;
//        Long totalOptionPriceForLastThreeMonths = 200L;
//        Long sumFinalAmountForCompletedOrders = 1000L;
//
//        when(clientOrderRepository.getTotalOptionPriceForLastThreeMonths(eq(clientId), any(LocalDateTime.class)))
//            .thenReturn(totalOptionPriceForLastThreeMonths);
//        when(clientOrderRepository.sumFinalAmountForCompletedOrders(eq(clientId), any(LocalDateTime.class)))
//            .thenReturn(sumFinalAmountForCompletedOrders);
//
//        PaymentGradeResponseDto result = paymentService.getPaymentRecordOfClient(clientId);
//
//        assertEquals(800L, result.getPaymentGradeValue());
//
//        verify(clientOrderRepository).getTotalOptionPriceForLastThreeMonths(eq(clientId), any(LocalDateTime.class));
//        verify(clientOrderRepository).sumFinalAmountForCompletedOrders(eq(clientId), any(LocalDateTime.class));
//    }
//    @Test
//     void testGetPostProcessRequiredPaymentResponseDto_Success() throws Exception {
//        String orderCode = "order123";
//        Long orderId = 1L;
//        Long clientId = 2L;
//        Long payAmount = 1000L;
//        headers.set("X-User-Id","2");
//        Order order = createOrder(1L, 5000L, 1000, OrderStatus.PAYED);
//
//        Payment payment = Payment.builder()
//            .payAmount(1000).build();
//        ProductOrderDetail productOrderDetail = ProductOrderDetail.builder().build();
//        List<ProductOrderDetail> productOrderDetailList = Collections.singletonList(productOrderDetail);
//
//        when(orderRepository.getOrderByOrderCode(orderCode)).thenReturn(Optional.of(order));
//        when(paymentRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.of(payment));
//        when(productOrderDetailRepository.findAllByOrder_OrderId(orderId)).thenReturn(productOrderDetailList);
//        // Assuming getClientId is a protected method and we can mock it directly in PaymentService class
//
//
//        PostProcessRequiredPaymentResponseDto result = paymentService.getPostProcessRequiredPaymentResponseDto(headers, orderCode);
//
//        assertEquals(orderId, result.getOrderId());
//        assertEquals(clientId, result.getClientId());
//        assertEquals(payAmount, result.getAmount());
//        assertEquals(1, result.getProductIdList().size());
//
//        verify(orderRepository).getOrderByOrderCode(orderCode);
//        verify(paymentRepository).findByOrder_OrderId(orderId);
//        verify(productOrderDetailRepository).findAllByOrder_OrderId(orderId);
//    }
//}
