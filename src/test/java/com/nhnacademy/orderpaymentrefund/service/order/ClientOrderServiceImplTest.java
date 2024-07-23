package com.nhnacademy.orderpaymentrefund.service.order;

import static java.lang.Long.parseLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelOrder;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.WrongClientAccessToOrder;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.impl.ClientOrderServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ClientOrderServiceImplTest {

    private static final String ID_HEADER = "X-User-Id";

    @Mock
    ProductOrderDetailRepository productOrderDetailRepository;
    @Mock
    ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    @Mock
    OrderRepository orderRepository;

    @Mock
    RedisTemplate<String, Object> redisTemplate;
    @Mock
    HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    ClientOrderServiceImpl clientOrderService;

    HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.set(ID_HEADER, "181");
    }

    @Test
    @DisplayName("회원 주문 임시 저장")
    void testSaveClientTemporalOrder() {

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        String tossOrderId = "uuid-1234";
        String redisOrder = "order";

        ClientOrderCreateForm requestDto = new ClientOrderCreateForm();
        ReflectionTestUtils.setField(requestDto, "tossOrderId", tossOrderId);

        clientOrderService.saveClientTemporalOrder(headers, requestDto);

        verify(hashOperations, times(1)).put(redisOrder, requestDto.getTossOrderId(), requestDto);
    }

    @Test
    @DisplayName("회원 주문 임시 데이터 가져오기")
    void testGetClientTemporalOrder() throws Exception {

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        String tossOrderId = "uuid-1234";
        String redisOrder = "order";

        ClientOrderCreateForm expectedOrder = new ClientOrderCreateForm();

        ReflectionTestUtils.setField(expectedOrder, "tossOrderId", tossOrderId);

        when(hashOperations.get(redisOrder, tossOrderId)).thenReturn(expectedOrder);

        ClientOrderCreateForm actualOrder = clientOrderService.getClientTemporalOrder(headers,
            tossOrderId);

        assertEquals(expectedOrder, actualOrder);

        verify(hashOperations, times(1)).get(redisOrder, tossOrderId);

    }

    @Test
    @DisplayName("회원 주문 페이지 조회 - 성공(오름차순)")
    void testGetASCOrdersSuccessTest() {
        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        int pageSize = 2;
        int pageNo = 0;
        String sortBy = "orderDatetime"; // 정렬 필드 이름 수정
        Sort sort = Sort.by(sortBy).ascending();

        // 주문 객체 생성
        Order order1 = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        Order order2 = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId321")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        List<Order> orderList = new ArrayList<>(List.of(order1, order2));
        Page<Order> orderPage = new PageImpl<>(orderList, PageRequest.of(pageNo, pageSize, sort),
            orderList.size());

        when(orderRepository.findByClientId(eq(clientId), any(PageRequest.class)))
            .thenReturn(orderPage);

        Page<ClientOrderGetResponseDto> response = clientOrderService.getOrders(headers, pageSize,
            pageNo, sortBy, "DESC");

        assertNotNull(response);
        assertEquals(orderList.size(), response.getTotalElements());
        assertEquals(pageSize, response.getSize());
        assertEquals(pageNo, response.getNumber());

        verify(orderRepository, times(1)).findByClientId(eq(clientId), any(PageRequest.class));

    }

    @Test
    @DisplayName("회원 주문 페이지 조회 - 성공(내림차순)")
    void testGetDESCOrdersSuccessTest() {
        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        int pageSize = 2;
        int pageNo = 0;
        String sortBy = "orderDatetime"; // 정렬 필드 이름 수정
        Sort sort = Sort.by(sortBy).ascending();

        // 주문 객체 생성
        Order order1 = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        Order order2 = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId321")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        List<Order> orderList = new ArrayList<>(List.of(order1, order2));
        Page<Order> orderPage = new PageImpl<>(orderList, PageRequest.of(pageNo, pageSize, sort),
            orderList.size());

        when(orderRepository.findByClientId(eq(clientId), any(PageRequest.class)))
            .thenReturn(orderPage);

        Page<ClientOrderGetResponseDto> response = clientOrderService.getOrders(headers, pageSize,
            pageNo, sortBy, "ASC");

        assertNotNull(response);
        assertEquals(orderList.size(), response.getTotalElements());
        assertEquals(pageSize, response.getSize());
        assertEquals(pageNo, response.getNumber());

        verify(orderRepository, times(1)).findByClientId(eq(clientId), any(PageRequest.class));

    }

    @Test
    @DisplayName("회원 주문 단건 조회 - 성공")
    void getOrderSuccessTest() {

        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(181L)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        ProductOrderDetail productOrderDetail = ProductOrderDetail.builder()
            .order(order)
            .productId(1L)
            .quantity(2L)
            .pricePerProduct(10000L)
            .productName("탕비실")
            .build();

        ProductOrderDetailOption productOrderDetailOption = ProductOrderDetailOption.builder()
            .productId(2L)
            .productOrderDetail(productOrderDetail)
            .optionProductName("오리 포장지")
            .optionProductPrice(10000L)
            .quantity(1L)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productOrderDetailRepository.findAllByOrder(order)).thenReturn(List.of(productOrderDetail));
        when(productOrderDetailOptionRepository.findFirstByProductOrderDetail(productOrderDetail)).thenReturn(productOrderDetailOption);

        ClientOrderGetResponseDto clientOrderGetResponseDto = clientOrderService.getOrder(headers, orderId);

        assertNotNull(clientOrderGetResponseDto);

    }

    @Test
    @DisplayName("회원 주문 단건 조회 - 실패(없는 주문을 조회하려고 시도할 때)")
    void getOrderFailTest() {
        long orderId = 123L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            clientOrderService.getOrder(headers, orderId);
        });
    }

    @Test
    @DisplayName("회원 주문 단건 조회 - 실패(요청한 사용자의 주문이 아닌 건에 대해 조회 시도할 때)")
    void getOtherClientOrderFailTest() {
        long orderId = 123L;

        Order order = Order.clientOrderBuilder()
            .clientId(1L)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        assertThrows(WrongClientAccessToOrder.class, () -> {
            clientOrderService.getOrder(headers, orderId);
        });
    }

    @Test
    @DisplayName("회원 주문 상태 취소 변경 - 실패(요청한 사용자의 주문이 아닌 건에 대해 조회 시도할 때)")
    void cancelOtherClientOrderFailTest(){

        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(1L)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        assertThrows(
            WrongClientAccessToOrder.class,  () -> {
                clientOrderService.cancelOrder(headers, orderId);
            }
        );
    }

    @Test
    @DisplayName("회원 주문 상태 취소 변경 - 실패(잘못된 변경)")
    void cancelWrongOrderStatusFailTest() {

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        assertThrows(CannotCancelOrder.class, () -> {
            clientOrderService.cancelOrder(headers, orderId);
        });

    }

    @Test
    @DisplayName("회원 주문 상태 취소 변경 - 성공")
    void cancelOrderStatusSuccessTest(){

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        clientOrderService.cancelOrder(headers, orderId);

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());

    }

    @Test
    @DisplayName("회원 주문 상태 환불 변경 - 실패(요청한 사용자의 주문이 아닌 건에 대해 조회 시도할 때)")
    void refundOtherClientOrderFailTest(){

        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(1L)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        assertThrows(
            WrongClientAccessToOrder.class,  () -> {
                clientOrderService.refundOrder(headers, orderId);
            }
        );
    }

    @Test
    @DisplayName("회원 주문 상태 환불 변경 - 실패(잘못된 변경)")
    void refundWrongOrderStatusFailTest() {

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PAYED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        assertThrows(CannotCancelOrder.class, () -> {
            clientOrderService.refundOrder(headers, orderId);
        });

    }

    @Test
    @DisplayName("회원 주문 상태 환불 변경 - 성공")
    void refundOrderStatusSuccessTest(){

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.REFUND_REQUEST);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        clientOrderService.refundOrder(headers, orderId);

        assertEquals(OrderStatus.REFUND, order.getOrderStatus());

    }

    @Test
    @DisplayName("회원 주문상태 반품 요청 변경 - 실패(요청한 사용자의 주문이 아닌 건에 대해 조회 시도할 때)")
    void refundRequestOtherClientOrderFailTest(){

        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(1L)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        assertThrows(
            WrongClientAccessToOrder.class,  () -> {
                clientOrderService.refundOrderRequest(headers, orderId);
            }
        );
    }

    @Test
    @DisplayName("회원 주문상태 반품 요청 변경 - 실패(잘못된 변경)")
    void refundRequestWrongOrderStatusFailTest() {

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PAYED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        assertThrows(CannotCancelOrder.class, () -> {
            clientOrderService.refundOrderRequest(headers, orderId);
        });

    }

    @Test
    @DisplayName("회원 주문상태 반품 요청 변경 - 성공")
    void refundRequestOrderStatusSuccessTest(){

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERY_COMPLETE);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        clientOrderService.refundOrderRequest(headers, orderId);

        assertEquals(OrderStatus.REFUND_REQUEST, order.getOrderStatus());

    }

    @Test
    @DisplayName("주문 상태 조회 - 성공")
    void testGetOrderStatusSuccess() {
        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 123L;

        Order order = Order.clientOrderBuilder()
            .clientId(clientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.WAIT_PAYMENT);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        String orderStatus = clientOrderService.getOrderStatus(headers, orderId);

        assertEquals(OrderStatus.WAIT_PAYMENT.kor, orderStatus);
    }

    @Test
    @DisplayName("주문 상태 조회 - 실패(주문이 존재하지 않음)")
    void testGetOrderStatusOrderNotFound() {
        long orderId = 123L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            clientOrderService.getOrderStatus(headers, orderId);
        });
    }

    @Test
    @DisplayName("주문 상태 조회 - 실패(잘못된 클라이언트 접근)")
    void testGetOrderStatusWrongClientAccess() {
        long orderId = 123L;
        long otherClientId = 1L;

        Order order = Order.clientOrderBuilder()
            .clientId(otherClientId)
            .couponId(null)
            .tossOrderId("tossOrderId123")
            .productTotalAmount(10000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("서울특별시 강남구")
            .discountAmountByCoupon(0L)
            .discountAmountByPoint(0L)
            .accumulatedPoint(0L)
            .build();
        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.WAIT_PAYMENT);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(WrongClientAccessToOrder.class, () -> {
            clientOrderService.getOrderStatus(headers, orderId);
        });
    }


}
