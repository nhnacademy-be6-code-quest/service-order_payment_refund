package com.nhnacademy.orderpaymentrefund.service.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
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

        when(hashOperations.get(REDIS_ORDER_KEY, anyString())).thenReturn(
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

        when(hashOperations.get(REDIS_ORDER_KEY, anyString())).thenReturn(
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
        when(hashOperations.get(REDIS_ORDER_KEY, anyString())).thenReturn(
            clientOrderCreateForm);

        PaymentOrderApproveRequestDto requestDto = orderService.getPaymentOrderApproveRequestDto(
            requestOrderCode);

        assertEquals(requestDto.getOrderCode(), clientOrderCreateForm.getOrderCode());
        assertEquals(requestDto.getOrderTotalAmount(), clientOrderCreateForm.getOrderTotalAmount());

    }

    @Test
    @DisplayName("결제 승인 요청을 받기 위한 데이터 조회 테스트 - 비회원")
    void getPaymentNonClientOrderApproveRequestDtoTest() {

        String requestOrderCode = "uuid-1234";
        String password = "1234";

        when(clientHeaderContext.isClient()).thenReturn(false);

        NonClientOrderForm nonClientOrderForm = createNonClientOrderForm(requestOrderCode,
            password);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(REDIS_ORDER_KEY, anyString())).thenReturn(nonClientOrderForm);

        PaymentOrderApproveRequestDto requestDto = orderService.getPaymentOrderApproveRequestDto(
            requestOrderCode);

        assertEquals(requestDto.getOrderTotalAmount(),
            nonClientOrderForm.getProductTotalAmount() + nonClientOrderForm.getShippingFee());
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

        long requestOrderId = 1L;
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

        // 회원 주문
        Order order1 = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order1, "orderId", 1L);
        ClientOrder clientOrder = createClientOrder(181L, 1L, 2000L,
            3000L, 500L, order1);
        ReflectionTestUtils.setField(clientOrder, "order", order1);
        ProductOrderDetail productOrderDetail1 = createProductOrderDetail(order1, 1L, 1L, 10000L,
            "상품1");
        List<ProductOrderDetail> clientOrderDetailList1 = new ArrayList<>(
            List.of(productOrderDetail1));
        ReflectionTestUtils.setField(order1, "productOrderDetailList", clientOrderDetailList1);

        // 비회원 주문
        Order order2 = createOrder("uuid-4321", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order2, "orderId", 2L);
        NonClientOrder nonClientOrder = createNonClientOrder("1234", "홍길동", "test@test.com",
            order2);
        ReflectionTestUtils.setField(nonClientOrder, "order", order2);
        ProductOrderDetail productOrderDetail2 = createProductOrderDetail(order2, 1L, 1L, 10000L,
            "상품1");
        List<ProductOrderDetail> clientOrderDetailList2 = new ArrayList<>(
            List.of(productOrderDetail2));
        ReflectionTestUtils.setField(order2, "productOrderDetailList", clientOrderDetailList2);

        List<Order> orderList = new ArrayList<>(List.of(order1, order2));
        Page<Order> orderPage = new PageImpl<>(orderList, PageRequest.of(pageNo, pageSize, sort),
            orderList.size());

        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(orderPage);
        when(clientOrderRepository.existsByOrder_OrderId(order1.getOrderId())).thenReturn(true);
        when(clientOrderRepository.existsByOrder_OrderId(order2.getOrderId())).thenReturn(false);
        when(productOrderDetailRepository.findAllByOrder_OrderId(order1.getOrderId())).thenReturn(
            order1.getProductOrderDetailList());
        when(productOrderDetailRepository.findAllByOrder_OrderId(order2.getOrderId())).thenReturn(
            order2.getProductOrderDetailList());
        when(clientOrderRepository.findByOrder_OrderId(order1.getOrderId())).thenReturn(
            Optional.of(clientOrder));
        when(nonClientOrderRepository.findByOrder_OrderId(order2.getOrderId())).thenReturn(
            Optional.of(nonClientOrder));

        Page<OrderResponseDto> responseDtoPage = orderService.getAllOrderList(pageSize, pageNo,
            sortBy, sortDir);

        assertEquals(responseDtoPage.getTotalElements(), orderPage.getTotalElements());

    }

    @Test
    @DisplayName("주문상품 상세 리스트 조회")
    void getProductOrderDetailListTest() {

        long orderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order, "orderId", orderId);
        ProductOrderDetail productOrderDetail = createProductOrderDetail(order, 1L, 1L, 10000L,
            "상품1");
        List<ProductOrderDetail> clientOrderDetailList = new ArrayList<>(
            List.of(productOrderDetail));
        ReflectionTestUtils.setField(order, "productOrderDetailList", clientOrderDetailList);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productOrderDetailRepository.findAllByOrder_OrderId(orderId)).thenReturn(
            order.getProductOrderDetailList());

        List<ProductOrderDetailResponseDto> expectedResponse = new ArrayList<>();
        for (ProductOrderDetail detail : order.getProductOrderDetailList()) {
            ProductOrderDetailResponseDto productOrderDetailResponseDto = ProductOrderDetailResponseDto.builder()
                .productOrderDetailId(detail.getProductOrderDetailId())
                .orderId(order.getOrderId())
                .productId(detail.getProductId())
                .quantity(detail.getQuantity())
                .pricePerProduct(detail.getPricePerProduct())
                .productName(detail.getProductName())
                .build();
            expectedResponse.add(productOrderDetailResponseDto);
        }

        List<ProductOrderDetailResponseDto> realResponse = orderService.getProductOrderDetailList(
            orderId);

        for (int i = 0; i < realResponse.size(); i++) {
            assertEquals(expectedResponse.get(i).getProductOrderDetailId(),
                realResponse.get(i).getProductOrderDetailId());
            assertEquals(expectedResponse.get(i).getOrderId(), realResponse.get(i).getOrderId());
            assertEquals(expectedResponse.get(i).getProductId(),
                realResponse.get(i).getProductId());
            assertEquals(expectedResponse.get(i).getQuantity(), realResponse.get(i).getQuantity());
            assertEquals(expectedResponse.get(i).getPricePerProduct(),
                realResponse.get(i).getPricePerProduct());
            assertEquals(expectedResponse.get(i).getProductName(),
                realResponse.get(i).getProductName());
        }

    }

    @Test
    @DisplayName("주문상품상세 단건 조회")
    void getProductOrderDetailTest() {

        long orderId = 1L;
        long productOrderDetailId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order, "orderId", orderId);
        ProductOrderDetail productOrderDetail = createProductOrderDetail(order, 1L, 1L, 10000L,
            "상품1");
        ReflectionTestUtils.setField(productOrderDetail, "productOrderDetailId",
            productOrderDetailId);
        List<ProductOrderDetail> orderDetailList = new ArrayList<>(List.of(productOrderDetail));
        ReflectionTestUtils.setField(order, "productOrderDetailList", orderDetailList);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productOrderDetailRepository.findById(productOrderDetailId)).thenReturn(
            Optional.of(productOrderDetail));

        ProductOrderDetailResponseDto expectedResponse = ProductOrderDetailResponseDto.builder()
            .productOrderDetailId(productOrderDetail.getProductId())
            .orderId(productOrderDetail.getOrder().getOrderId())
            .productId(productOrderDetail.getProductId())
            .quantity(productOrderDetail.getQuantity())
            .pricePerProduct(productOrderDetail.getPricePerProduct())
            .productName(productOrderDetail.getProductName())
            .build();

        ProductOrderDetailResponseDto realResponse = orderService.getProductOrderDetail(orderId,
            productOrderDetailId);

        assertEquals(expectedResponse.getProductOrderDetailId(),
            realResponse.getProductOrderDetailId());
        assertEquals(expectedResponse.getOrderId(), realResponse.getOrderId());
        assertEquals(expectedResponse.getProductId(), realResponse.getProductId());
        assertEquals(expectedResponse.getQuantity(), realResponse.getQuantity());
        assertEquals(expectedResponse.getPricePerProduct(), realResponse.getPricePerProduct());
        assertEquals(expectedResponse.getProductName(), realResponse.getProductName());

    }

    @Test
    @DisplayName("주문옵션상품 단건 조회")
    void getProductOrderDetailOptionResponseDtoTest() {

        long orderId = 1L;
        long productOrderDetailId = 1L;
        long productOrderDetailOptionId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order, "orderId", orderId);
        ProductOrderDetail productOrderDetail = createProductOrderDetail(order, 1L, 1L, 10000L,
            "상품1");
        ReflectionTestUtils.setField(productOrderDetail, "productOrderDetailId",
            productOrderDetailId);
        List<ProductOrderDetail> orderDetailList = new ArrayList<>(List.of(productOrderDetail));
        ReflectionTestUtils.setField(order, "productOrderDetailList", orderDetailList);
        ProductOrderDetailOption productOrderDetailOption = createProductOrderDetailOption(2L,
            productOrderDetail, "포장지", 500L, 1L);
        ReflectionTestUtils.setField(productOrderDetailOption, "productOrderDetailOptionId",
            productOrderDetailOptionId);

        ProductOrderDetailOptionResponseDto expectedResponse = ProductOrderDetailOptionResponseDto.builder()
            .productId(productOrderDetailOption.getProductId())
            .productOrderDetailId(productOrderDetailOption.getProductOrderDetail().getProductId())
            .optionProductName(productOrderDetailOption.getOptionProductName())
            .optionProductPrice(productOrderDetailOption.getOptionProductPrice())
            .optionProductQuantity(productOrderDetailOption.getQuantity())
            .build();

        when(productOrderDetailRepository.findById(productOrderDetailId)).thenReturn(
            Optional.of(productOrderDetail));
        when(productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
            productOrderDetailId)).thenReturn(Optional.of(productOrderDetailOption));

        ProductOrderDetailOptionResponseDto realResponse = orderService.getProductOrderDetailOptionResponseDto(
            orderId, productOrderDetailId);

        assertEquals(expectedResponse.getProductId(), realResponse.getProductId());
        assertEquals(expectedResponse.getProductOrderDetailId(),
            realResponse.getProductOrderDetailId());
        assertEquals(expectedResponse.getOptionProductName(), realResponse.getOptionProductName());
        assertEquals(expectedResponse.getOptionProductPrice(),
            realResponse.getOptionProductPrice());
        assertEquals(expectedResponse.getOptionProductQuantity(),
            realResponse.getOptionProductQuantity());

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

}
