package com.nhnacademy.orderpaymentrefund.service.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.domain.order.ClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.InvalidOrderChangeAttempt;
import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.NonClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.impl.OrderServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private static final String ID_HEADER = "X-User-Id";
    private static final String REDIS_ORDER_KEY = "order";

    @Spy
    ObjectMapper objectMapper;

    @Mock
    ClientHeaderContext clientHeaderContext;

    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductOrderDetailRepository productOrderDetailRepository;
    @Mock
    ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    @Mock
    ClientOrderRepository clientOrderRepository;
    @Mock
    NonClientOrderRepository nonClientOrderRepository;

    @Mock
    RedisTemplate<String, Object> redisTemplate;
    @Mock
    HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    @DisplayName("결제창에 보일 데이터 조회 테스트 - 회원 주문건")
    void getPaymentClientOrderShowRequestDtoTest() {

        String requestOrderCode = "uuid-1234";
        ClientOrderCreateForm clientOrderCreateForm = createClientOrderCreateForm(requestOrderCode);

        when(clientHeaderContext.isClient()).thenReturn(true);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        when(hashOperations.get(eq(REDIS_ORDER_KEY), anyString())).thenReturn(
            clientOrderCreateForm);

        PaymentOrderShowRequestDto requestDto = orderService.getPaymentOrderShowRequestDto(
            requestOrderCode);

        PaymentOrderShowRequestDto expectedRequestDto = createPaymentOrderShowRequestDto(
            clientOrderCreateForm.getOrderTotalAmount(),
            clientOrderCreateForm.getCouponDiscountAmount(),
            clientOrderCreateForm.getUsedPointDiscountAmount(),
            clientOrderCreateForm.getOrderCode(), "상품1외 1개");

        assertEquals(expectedRequestDto.getOrderCode(), requestDto.getOrderCode());
        assertEquals(expectedRequestDto.getOrderTotalAmount(), requestDto.getOrderTotalAmount());
        assertEquals(expectedRequestDto.getOrderHistoryTitle(), requestDto.getOrderHistoryTitle());
        assertEquals(expectedRequestDto.getDiscountAmountByCoupon(),
            requestDto.getDiscountAmountByCoupon());
        assertEquals(expectedRequestDto.getDiscountAmountByPoint(),
            requestDto.getDiscountAmountByPoint());

    }

    @Test
    @DisplayName("결제창에 보일 데이터 조회 테스트 - 비회원 주문건")
    void getPaymentNonClientOrderShowRequestDtoTest() {

        String requestOrderCode = "uuid-1234";
        String requestPassword = "1234";

        NonClientOrderForm nonClientOrderForm = createNonClientOrderForm(requestOrderCode,
            requestPassword);

        when(clientHeaderContext.isClient()).thenReturn(false);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        when(hashOperations.get(eq(REDIS_ORDER_KEY), anyString())).thenReturn(
            nonClientOrderForm);

        PaymentOrderShowRequestDto requestDto = orderService.getPaymentOrderShowRequestDto(
            requestOrderCode);

        PaymentOrderShowRequestDto expectedRequestDto = createPaymentOrderShowRequestDto(
            nonClientOrderForm.getProductTotalAmount() + nonClientOrderForm.getShippingFee(),
            0L,
            0L,
            nonClientOrderForm.getOrderCode(), "상품1외 1개");

        assertEquals(expectedRequestDto.getOrderCode(), requestDto.getOrderCode());
        assertEquals(expectedRequestDto.getOrderTotalAmount(), requestDto.getOrderTotalAmount());
        assertEquals(expectedRequestDto.getOrderHistoryTitle(), requestDto.getOrderHistoryTitle());
        assertEquals(expectedRequestDto.getDiscountAmountByCoupon(),
            requestDto.getDiscountAmountByCoupon());
        assertEquals(expectedRequestDto.getDiscountAmountByPoint(),
            requestDto.getDiscountAmountByPoint());

    }

    @Test
    @DisplayName("결제 승인 요청을 받기 위한 데이터 조회 테스트 - 회원")
    void getPaymentClientOrderApproveRequestDtoTest() {

        String requestOrderCode = "uuid-1234";
        Long requestClientId = 181L;

        ClientOrderCreateForm clientOrderCreateForm = createClientOrderCreateForm(requestOrderCode);

        when(clientHeaderContext.isClient()).thenReturn(true);
        when(clientHeaderContext.getClientId()).thenReturn(requestClientId);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(eq(REDIS_ORDER_KEY), anyString())).thenReturn(clientOrderCreateForm);

        PaymentOrderApproveRequestDto requestDto = orderService.getPaymentOrderApproveRequestDto(requestOrderCode);

        assertEquals(requestDto.getOrderCode(), clientOrderCreateForm.getOrderCode());
        assertEquals(requestDto.getOrderTotalAmount(), clientOrderCreateForm.getOrderTotalAmount());

    }

    @Test
    @DisplayName("결제 승인 요청을 받기 위한 데이터 조회 테스트 - 비회원")
    void getPaymentNonClientOrderApproveRequestDtoTest() {

        String requestOrderCode = "uuid-1234";
        String password = "1234";

        when(clientHeaderContext.isClient()).thenReturn(false);

        NonClientOrderForm nonClientOrderForm = createNonClientOrderForm(requestOrderCode, password);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(eq(REDIS_ORDER_KEY), anyString())).thenReturn(nonClientOrderForm);

        PaymentOrderApproveRequestDto requestDto = orderService.getPaymentOrderApproveRequestDto(requestOrderCode);

        assertEquals(requestDto.getOrderTotalAmount(), nonClientOrderForm.getProductTotalAmount() + nonClientOrderForm.getShippingFee());
        assertEquals(requestDto.getOrderCode(), nonClientOrderForm.getOrderCode());

    }

    @Test
    @DisplayName("주문 상태 변경 테스트 - 성공")
    void changeOrderStatusSuccessTest() {

        Long requestOrderId = 1L;
        String orderCode = "uuid-1234";
        String phoneNumber = "01012341234";
        String deliveryAddress = "광주 광역시 어쩌구 저쩌구";

        Order order = createOrder(orderCode, 15000L, 3000, null, phoneNumber, deliveryAddress);

        // 결제 대기 -> 결제완료
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.WAIT_PAYMENT);

        String requestOrderStatusKor = "결제완료";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);

        // 결제완료 -> 배송중
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PAYED);

        requestOrderStatusKor = "배송중";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);

        // 배송중 -> 배송완료
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERING);

        requestOrderStatusKor = "배송완료";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);


        // 결제완료 -> 배송중
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PAYED);

        requestOrderStatusKor = "배송중";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);


        // 결제대기 -> 주문취소
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.WAIT_PAYMENT);

        requestOrderStatusKor = "주문취소";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);


        // 결제완료 -> 주문취소
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PAYED);

        requestOrderStatusKor = "주문취소";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);


        // 배송중 -> 반품요청
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERING);

        requestOrderStatusKor = "반품요청";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);

        // 배송완료 -> 반품요청
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERY_COMPLETE);

        requestOrderStatusKor = "반품요청";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);


        // 반품요청 -> 반품
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.REFUND_REQUEST);

        requestOrderStatusKor = "반품";
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(requestOrderId, requestOrderStatusKor);

        assertEquals(order.getOrderStatus().kor, requestOrderStatusKor);

    }

    @Test
    @DisplayName("주문 상태 변경 테스트 - 실패")
    void changeOrderStatusFailureTest() {

        Long requestOrderId = 1L;
        String orderCode = "uuid-1234";
        String phoneNumber = "01012341234";
        String deliveryAddress = "광주 광역시 어쩌구 저쩌구";

        Order order = createOrder(orderCode, 15000L, 3000, null, phoneNumber, deliveryAddress);

        // 배송완료 -> 결제완료 (유효하지 않은 상태 변경)
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERY_COMPLETE);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderChangeAttempt.class, () -> {
            orderService.changeOrderStatus(requestOrderId, "결제완료");
        });

        // 결제완료 -> 반품 (유효하지 않은 상태 변경)
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PAYED);
        
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderChangeAttempt.class, () -> {
            orderService.changeOrderStatus(requestOrderId, "반품");
        });

        // 결제대기 -> 배송완료 (유효하지 않은 상태 변경)
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.WAIT_PAYMENT);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderChangeAttempt.class, () -> {
            orderService.changeOrderStatus(requestOrderId, "배송완료");
        });

        // 주문취소 -> 결제완료 (유효하지 않은 상태 변경)
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.CANCEL);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderChangeAttempt.class, () -> {
            orderService.changeOrderStatus(requestOrderId, "결제완료");
        });

        // 반품 -> 배송완료 (유효하지 않은 상태 변경)
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.REFUND);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderChangeAttempt.class, () -> {
            orderService.changeOrderStatus(requestOrderId, "배송완료");
        });
    }

    @Test
    @DisplayName("회원 및 비회원 주문 목록 조회를 위한 테스트")
    void getAllOrderListTest() {

        int pageSize = 2;
        int pageNo = 0;
        String sortBy = "orderDatetime";
        String sortDir = "asc";

        Sort sort = Sort.by(sortBy, sortDir);

        Order order1 = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        Order order2 = createOrder("uuid-4321", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");

        List<Order> orderList = new ArrayList<>(List.of(order1, order2));
        Page<Order> orderPage = new PageImpl<>(orderList, PageRequest.of(pageNo, pageSize, sort), orderList.size());







    }

    @Test
    @DisplayName("주문상품 상세 리스트 조회")
    void getProductOrderDetailListTest() {

    }

    @Test
    @DisplayName("주문상품상세 단건 조회")
    void getProductOrderDetailTest() {

    }

    @Test
    @DisplayName("주문옵션상품 단건 조회")
    void getProductOrderDetailOptionResponseDtoTest() {

    }

    private Order createOrder(String orderCode, Long productTotalAmount, Integer shippingFee,
        LocalDate designatedDeliveryDate,
        String phoneNumber, String deliveryAddress) {
        return Order.builder()
            .orderCode(orderCode)
            .productTotalAmount(productTotalAmount)
            .shippingFee(shippingFee)
            .designatedDeliveryDate(designatedDeliveryDate)
            .phoneNumber(phoneNumber)
            .deliveryAddress(deliveryAddress)
            .build();
    }

    private ClientOrder createClientOrder(Long clientId, Long couponId, Long discountAmountByCoupon,
        Long discountAmountByPoint, Long accumulatedPoint, Order order) {
        return ClientOrder.builder()
            .clientId(clientId)
            .couponId(couponId)
            .discountAmountByCoupon(discountAmountByCoupon)
            .discountAmountByPoint(discountAmountByPoint)
            .accumulatedPoint(accumulatedPoint)
            .order(order)
            .build();
    }

    private NonClientOrder createNonClientOrder(String nonClientOrderPassword,
        String nonClientOrdererName, String nonClientOrdererEmail, Order order) {
        return NonClientOrder.builder()
            .nonClientOrdererEmail(nonClientOrdererEmail)
            .nonClientOrdererName(nonClientOrdererName)
            .nonClientOrderPassword(nonClientOrderPassword)
            .build();
    }

    private ProductOrderDetail createProductOrderDetail(Order order, Long productId, Long quantity,
        Long pricePerProduct, String productName) {
        return ProductOrderDetail.builder()
            .order(order)
            .productId(productId)
            .quantity(quantity)
            .pricePerProduct(pricePerProduct)
            .productName(productName)
            .build();
    }

    private ProductOrderDetailOption createProductOrderDetailOption(long productId,
        ProductOrderDetail productOrderDetail, String optionProductName, long optionProductPrice,
        long quantity) {
        return ProductOrderDetailOption.builder()
            .productId(productId)
            .productOrderDetail(productOrderDetail)
            .optionProductName(optionProductName)
            .optionProductPrice(optionProductPrice)
            .quantity(quantity)
            .build();
    }

    private OrderDetailDtoItem createOrderDetailDtoItem(Long productId, String productName,
        Long quantity, Long productSinglePrice) {

        OrderDetailDtoItem orderDetailDtoItem = new OrderDetailDtoItem();

        ReflectionTestUtils.setField(orderDetailDtoItem, "productId", productId);
        ReflectionTestUtils.setField(orderDetailDtoItem, "productName", productName);
        ReflectionTestUtils.setField(orderDetailDtoItem, "quantity", quantity);
        ReflectionTestUtils.setField(orderDetailDtoItem, "productSinglePrice", productSinglePrice);
        ReflectionTestUtils.setField(orderDetailDtoItem, "packableProduct", false);
        ReflectionTestUtils.setField(orderDetailDtoItem, "usePackaging", false);

        return orderDetailDtoItem;
    }

    private ClientOrderCreateForm createClientOrderCreateForm(String orderCode) {

        ClientOrderCreateForm clientOrderCreateForm = new ClientOrderCreateForm();

        OrderDetailDtoItem orderDetailDtoItem1 = createOrderDetailDtoItem(1L, "상품1", 1L, 5000L);
        OrderDetailDtoItem orderDetailDtoItem2 = createOrderDetailDtoItem(2L, "상품2", 1L, 5000L);

        List<OrderDetailDtoItem> orderDetailDtoItemList = new ArrayList<>();
        orderDetailDtoItemList.add(orderDetailDtoItem1);
        orderDetailDtoItemList.add(orderDetailDtoItem2);

        ReflectionTestUtils.setField(clientOrderCreateForm, "orderDetailDtoItemList",
            orderDetailDtoItemList);
        ReflectionTestUtils.setField(clientOrderCreateForm, "couponId", 1L);
        ReflectionTestUtils.setField(clientOrderCreateForm, "shippingFee", 2500);
        ReflectionTestUtils.setField(clientOrderCreateForm, "productTotalAmount", 10000L);
        ReflectionTestUtils.setField(clientOrderCreateForm, "orderTotalAmount", 12500L);
        ReflectionTestUtils.setField(clientOrderCreateForm, "payAmount", 10000L);
        ReflectionTestUtils.setField(clientOrderCreateForm, "couponDiscountAmount", 2000L);
        ReflectionTestUtils.setField(clientOrderCreateForm, "usedPointDiscountAmount", 500L);
        ReflectionTestUtils.setField(clientOrderCreateForm, "orderedPersonName", "홍길동");
        ReflectionTestUtils.setField(clientOrderCreateForm, "phoneNumber", "01012341234");
        ReflectionTestUtils.setField(clientOrderCreateForm, "deliveryAddress", "광주 광역시 어쩌구 저쩌구");
        ReflectionTestUtils.setField(clientOrderCreateForm, "paymentMethod", "toss");
        ReflectionTestUtils.setField(clientOrderCreateForm, "accumulatePoint", 500L);
        ReflectionTestUtils.setField(clientOrderCreateForm, "orderCode", orderCode);

        return clientOrderCreateForm;

    }

    private NonClientOrderForm createNonClientOrderForm(String orderCode, String password) {

        NonClientOrderForm nonClientOrderForm = new NonClientOrderForm();

        OrderDetailDtoItem orderDetailDtoItem1 = createOrderDetailDtoItem(1L, "상품1", 1L, 5000L);
        OrderDetailDtoItem orderDetailDtoItem2 = createOrderDetailDtoItem(2L, "상품2", 1L, 5000L);

        List<OrderDetailDtoItem> orderDetailDtoItemList = new ArrayList<>();
        orderDetailDtoItemList.add(orderDetailDtoItem1);
        orderDetailDtoItemList.add(orderDetailDtoItem2);

        ReflectionTestUtils.setField(nonClientOrderForm, "orderDetailDtoItemList",
            orderDetailDtoItemList);
        ReflectionTestUtils.setField(nonClientOrderForm, "shippingFee", 5000);
        ReflectionTestUtils.setField(nonClientOrderForm, "productTotalAmount", 10000L);
        ReflectionTestUtils.setField(nonClientOrderForm, "payAmount", 15000L);
        ReflectionTestUtils.setField(nonClientOrderForm, "orderedPersonName", "비회원 아무개");
        ReflectionTestUtils.setField(nonClientOrderForm, "email", "test@test.com");
        ReflectionTestUtils.setField(nonClientOrderForm, "phoneNumber", "01012341234");
        ReflectionTestUtils.setField(nonClientOrderForm, "addressZipCode", "1234");
        ReflectionTestUtils.setField(nonClientOrderForm, "deliveryAddress", "광주 광역시 어쩌구저쩌구 아파트");
        ReflectionTestUtils.setField(nonClientOrderForm, "deliveryDetailAddress", "101동 101호");
        ReflectionTestUtils.setField(nonClientOrderForm, "paymentMethod", "toss");
        ReflectionTestUtils.setField(nonClientOrderForm, "orderPassword", password);
        ReflectionTestUtils.setField(nonClientOrderForm, "orderCode", orderCode);

        return nonClientOrderForm;
    }

    private PaymentOrderShowRequestDto createPaymentOrderShowRequestDto(long orderTotalAmount,
        long discountAmountByCoupon, long discountAmountByPoint, String orderCode,
        String orderHistoryTitle) {

        return PaymentOrderShowRequestDto.builder()
            .orderTotalAmount(orderTotalAmount)
            .discountAmountByCoupon(discountAmountByCoupon)
            .discountAmountByPoint(discountAmountByPoint)
            .orderCode(orderCode)
            .orderHistoryTitle(orderHistoryTitle)
            .build();

    }

//    private PaymentOrderApproveRequestDto createPaymentOrderApproveRequestDto(ClientOrderCreateForm clientOrderCreateForm, Long clientId) {
//
//        List<ProductOrderDetailRequestDto> productOrderDetailRequestDtoList = new ArrayList<>();
//
//        for(OrderDetailDtoItem orderDetailDtoItem : clientOrderCreateForm.getOrderDetailDtoItemList()){
//
//            List<ProductOrderDetailOptionRequestDto> productOrderDetailOptionRequestDtoList = new ArrayList<>();
//            ProductOrderDetailOptionRequestDto productOrderDetailOptionRequestDto = ProductOrderDetailOptionRequestDto.builder()
//                .productId(orderDetailDtoItem.getOptionProductId())
//                .optionProductQuantity(orderDetailDtoItem.getQuantity())
//                .build();
//            productOrderDetailOptionRequestDtoList.add(productOrderDetailOptionRequestDto);
//
//            ProductOrderDetailRequestDto productOrderDetailRequestDto = ProductOrderDetailRequestDto.builder()
//                .productId(orderDetailDtoItem.getProductId())
//                .quantity(orderDetailDtoItem.getQuantity())
//                .productOrderDetailOptionRequestDtoList(productOrderDetailOptionRequestDtoList)
//                .build();
//
//            productOrderDetailRequestDtoList.add(productOrderDetailRequestDto);
//        }
//
//        return PaymentOrderApproveRequestDto.builder()
//            .orderTotalAmount(clientOrderCreateForm.getOrderTotalAmount())
//            .discountAmountByPoint(clientOrderCreateForm.getUsedPointDiscountAmount())
//            .discountAmountByCoupon(clientOrderCreateForm.getCouponDiscountAmount())
//            .orderCode(clientOrderCreateForm.getOrderCode())
//            .clientId(clientId)
//            .couponId(clientOrderCreateForm.getCouponId())
//            .accumulatedPoint(clientOrderCreateForm.getAccumulatePoint())
//            .productOrderDetailList(productOrderDetailRequestDtoList)
//            .build();
//    }



}
