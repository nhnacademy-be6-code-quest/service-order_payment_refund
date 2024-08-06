package com.nhnacademy.orderpaymentrefund.service.order;

import static java.lang.Long.parseLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.orderpaymentrefund.client.coupon.CouponClient;
import com.nhnacademy.orderpaymentrefund.client.product.ProductClient;
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.domain.order.ClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.client.ProductGetNameAndPriceResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CouponDiscountInfoRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.CouponOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.CouponOrderResponseDto.CategoryCoupon;
import com.nhnacademy.orderpaymentrefund.dto.order.response.CouponOrderResponseDto.CouponPolicyDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.CouponOrderResponseDto.ProductCoupon;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderCouponDiscountInfo;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelOrder;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.WrongClientAccessToOrder;
import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.impl.ClientOrderServiceImpl;
import java.time.LocalDate;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ClientOrderServiceImplTest {

    private static final String ID_HEADER = "X-User-Id";

    @Mock
    private ClientHeaderContext clientHeaderContext;

    @Mock
    ProductOrderDetailRepository productOrderDetailRepository;
    @Mock
    ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    @Mock
    ClientOrderRepository clientOrderRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    CouponClient couponClient;
    @Mock
    ProductClient productClient;

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
        String orderCode = "uuid-1234";
        String redisOrder = "order";

        ClientOrderCreateForm requestDto = new ClientOrderCreateForm();
        ReflectionTestUtils.setField(requestDto, "orderCode", orderCode);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        doNothing().when(hashOperations).put(eq(redisOrder), anyString(), any(ClientOrderCreateForm.class));

        clientOrderService.saveClientTemporalOrder(headers, requestDto);

        verify(hashOperations, times(1)).put(redisOrder, requestDto.getOrderCode(), requestDto);
    }


    @Test
    @DisplayName("회원 주문 임시 데이터 가져오기")
    void testGetClientTemporalOrder() throws Exception {

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        String orderCode = "uuid-1234";
        String redisOrder = "order";

        ClientOrderCreateForm expectedOrder = new ClientOrderCreateForm();

        ReflectionTestUtils.setField(expectedOrder, "orderCode", orderCode);

        when(hashOperations.get(redisOrder, orderCode)).thenReturn(expectedOrder);

        ClientOrderCreateForm actualOrder = clientOrderService.getClientTemporalOrder(headers,
            orderCode);

        assertEquals(expectedOrder, actualOrder);

        verify(hashOperations, times(1)).get(redisOrder, orderCode);

    }


    @Test
    @DisplayName("회원 주문 페이지 조회 - 성공(오름차순)")
    void testGetASCOrdersSuccessTest() {
        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        int pageSize = 2;
        int pageNo = 0;
        String sortBy = "orderDatetime"; // 정렬 필드 이름 수정
        Sort sort = Sort.by(sortBy).ascending();

        // 주문 객체 생성
        Order order1 = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder1 = createClientOrder(181L, 1L, 2000L,
            3000L, 500L, order1);

        Order order2 = createOrder("uuid-4321", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder2 = createClientOrder(181L, 1L, 2000L,
            3000L, 500L, order2);

        List<ClientOrder> clientOrderList = new ArrayList<>(List.of(clientOrder1, clientOrder2));
        Page<ClientOrder> orderPage = new PageImpl<>(clientOrderList,
            PageRequest.of(pageNo, pageSize, sort),
            clientOrderList.size());

        when(clientOrderRepository.findByClientId(eq(clientId), any(PageRequest.class)))
            .thenReturn(orderPage);

        Page<ClientOrderGetResponseDto> response = clientOrderService.getOrders(headers, pageSize,
            pageNo, sortBy, "DESC");

        assertNotNull(response);
        assertEquals(clientOrderList.size(), response.getTotalElements());
        assertEquals(pageSize, response.getSize());
        assertEquals(pageNo, response.getNumber());

        verify(clientOrderRepository, times(1)).findByClientId(eq(clientId),
            any(PageRequest.class));

    }


    @Test
    @DisplayName("회원 주문 페이지 조회 - 성공(내림차순)")
    void testGetDESCOrdersSuccessTest() {
        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        int pageSize = 2;
        int pageNo = 0;
        String sortBy = "orderDatetime"; // 정렬 필드 이름 수정
        Sort sort = Sort.by(sortBy).ascending();

        Order order1 = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder1 = createClientOrder(181L, 1L, 2000L,
            3000L, 500L, order1);

        Order order2 = createOrder("uuid-4321", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder2 = createClientOrder(181L, 1L, 2000L,
            3000L, 500L, order2);

        List<ClientOrder> clientOrderList = new ArrayList<>(List.of(clientOrder1, clientOrder2));
        Page<ClientOrder> orderPage = new PageImpl<>(clientOrderList,
            PageRequest.of(pageNo, pageSize, sort),
            clientOrderList.size());

        when(clientOrderRepository.findByClientId(eq(clientId), any(PageRequest.class)))
            .thenReturn(orderPage);

        Page<ClientOrderGetResponseDto> response = clientOrderService.getOrders(headers, pageSize,
            pageNo, sortBy, "ASC");

        assertNotNull(response);
        assertEquals(clientOrderList.size(), response.getTotalElements());
        assertEquals(pageSize, response.getSize());
        assertEquals(pageNo, response.getNumber());

        verify(clientOrderRepository, times(1)).findByClientId(eq(clientId),
            any(PageRequest.class));

    }


    @Test
    @DisplayName("회원 주문 단건 조회 - 성공")
    void getOrderSuccessTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long orderId = 1L;
        long productOrderDetailId = 1L;
        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);

        ProductOrderDetail productOrderDetail = createProductOrderDetail(order, 1L, 2L, 10000L,
            "탕비실");
        ProductOrderDetailOption productOrderDetailOption = createProductOrderDetailOption(2L,
            productOrderDetail, "포장지", 500L, 1L);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.of(clientOrder));
        when(productOrderDetailRepository.findAllByOrder_OrderId(orderId)).thenReturn(
            List.of(productOrderDetail));
        when(productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
            anyLong())).thenReturn(Optional.of(productOrderDetailOption));

        ClientOrderGetResponseDto clientOrderGetResponseDto = clientOrderService.getOrder(headers,
            orderId);

        assertNotNull(clientOrderGetResponseDto);

    }


    @Test
    @DisplayName("회원 주문 단건 조회 - 실패(없는 주문을 조회하려고 시도할 때)")
    void getOrderFailTest() {

        long orderId = 123L;

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            clientOrderService.getOrder(headers, orderId);
        });

    }


    @Test
    @DisplayName("회원 주문 단건 조회 - 실패(요청한 사용자의 주문이 아닌 건에 대해 조회 시도할 때)")
    void getOtherClientOrderFailTest() {
        long orderId = 123L;

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(1L, 1L, 2000L,
            3000L, 500L, order);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.ofNullable(clientOrder));

        assertThrows(WrongClientAccessToOrder.class, () -> {
            clientOrderService.getOrder(headers, orderId);
        });
    }


    @Test
    @DisplayName("회원 주문 상태 취소 변경 - 실패(요청한 사용자의 주문이 아닌 건에 대해 조회 시도할 때)")
    void cancelOtherClientOrderFailTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long orderId = 1L;
        long wrongClientId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(wrongClientId, 1L, 2000L,
            3000L, 500L, order);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.ofNullable(clientOrder));

        assertThrows(
            WrongClientAccessToOrder.class, () -> {
                clientOrderService.cancelOrder(headers, orderId);
            }
        );
    }


    @Test
    @DisplayName("회원 주문 상태 취소 변경 - 실패(잘못된 변경)")
    void cancelWrongOrderStatusFailTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERING);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.ofNullable(clientOrder));

        assertThrows(CannotCancelOrder.class, () -> {
            clientOrderService.cancelOrder(headers, orderId);
        });

    }


    @Test
    @DisplayName("회원 주문 상태 취소 변경 - 성공")
    void cancelOrderStatusSuccessTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);

        when(clientOrderRepository.findByOrder_OrderId(anyLong())).thenReturn(
            Optional.ofNullable(clientOrder));

        clientOrderService.cancelOrder(headers, orderId);

        assertEquals(OrderStatus.CANCEL, order.getOrderStatus());

    }


    @Test
    @DisplayName("회원 주문 상태 환불 변경 - 실패(요청한 사용자의 주문이 아닌 건에 대해 조회 시도할 때)")
    void refundOtherClientOrderFailTest() {

        long orderId = 1L;
        long wrongClientId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(wrongClientId, 1L, 2000L,
            3000L, 500L, order);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.ofNullable(clientOrder));

        assertThrows(
            WrongClientAccessToOrder.class, () -> {
                clientOrderService.refundOrder(headers, orderId);
            }
        );
    }


    @Test
    @DisplayName("회원 주문 상태 환불 변경 - 실패(잘못된 변경)")
    void refundWrongOrderStatusFailTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PAYED);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.ofNullable(clientOrder));

        assertThrows(CannotCancelOrder.class, () -> {
            clientOrderService.refundOrder(headers, orderId);
        });

    }


    @Test
    @DisplayName("회원 주문 상태 환불 변경 - 성공")
    void refundOrderStatusSuccessTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.REFUND_REQUEST);

        when(clientOrderRepository.findByOrder_OrderId(anyLong())).thenReturn(
            Optional.ofNullable(clientOrder));

        clientOrderService.refundOrder(headers, orderId);

        assertEquals(OrderStatus.REFUND, order.getOrderStatus());

    }


    @Test
    @DisplayName("회원 주문상태 반품 요청 변경 - 실패(요청한 사용자의 주문이 아닌 건에 대해 조회 시도할 때)")
    void refundRequestOtherClientOrderFailTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long orderId = 1L;
        long wrongClientId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(wrongClientId, 1L, 2000L,
            3000L, 500L, order);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.ofNullable(clientOrder));

        assertThrows(
            WrongClientAccessToOrder.class, () -> {
                clientOrderService.refundOrderRequest(headers, orderId);
            }
        );
    }


    @Test
    @DisplayName("회원 주문상태 반품 요청 변경 - 실패(잘못된 변경)")
    void refundRequestWrongOrderStatusFailTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.PAYED);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.ofNullable(clientOrder));

        assertThrows(CannotCancelOrder.class, () -> {
            clientOrderService.refundOrderRequest(headers, orderId);
        });

    }


    @Test
    @DisplayName("회원 주문상태 반품 요청 변경 - 성공")
    void refundRequestOrderStatusSuccessTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERY_COMPLETE);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(
            Optional.ofNullable(clientOrder));
        when(orderRepository.save(order)).thenReturn(order);

        clientOrderService.refundOrderRequest(headers, orderId);

        assertEquals(OrderStatus.REFUND_REQUEST, order.getOrderStatus());

    }

    @Test
    @DisplayName("주문상품 상세 리스트 조회")
    void getProductOrderDetailResponseDtoListTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long orderId = 123L;
        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);
        ProductOrderDetail productOrderDetail = createProductOrderDetail(order, 1L, 2L, 10000L,
            "탕비실");

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.of(clientOrder));
        when(productOrderDetailRepository.findAllByOrder_OrderId(anyLong())).thenReturn(
            List.of(productOrderDetail));

        List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList = clientOrderService.getProductOrderDetailResponseDtoList(
            headers, orderId);

        assertNotNull(productOrderDetailResponseDtoList);
        assertEquals(1, productOrderDetailResponseDtoList.size());

    }


    @Test
    @DisplayName("주문상품 상세 리스트 조회 - 성공")
    void getProductOrderDetailResponseDtoListExceptionSuccessTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long orderId = 1L;
        long productOrderDetailId = 2L;
        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(clientId, 1L, 2000L,
            3000L, 500L, order);
        ProductOrderDetail productOrderDetail = createProductOrderDetail(order, 1L, 2L, 10000L,
            "탕비실");
        ProductOrderDetailOption productOrderDetailOption = createProductOrderDetailOption(2L,
            productOrderDetail, "포장지", 500L, 1L);

        when(clientOrderRepository.findByOrder_OrderId(anyLong())).thenReturn(Optional.of(clientOrder));
        when(productOrderDetailRepository.findById(anyLong())).thenReturn(
            Optional.ofNullable(productOrderDetail));

        ProductOrderDetailResponseDto productOrderDetailResponseDto = clientOrderService.getProductOrderDetailResponseDto(
            headers, orderId, productOrderDetailId);

        assertNotNull(productOrderDetailResponseDto);

    }

    @Test
    @DisplayName("주문상품 상세 리스트 조회 - 실패")
    void getProductOrderDetailResponseDtoListExceptionFailTest() {

        when(clientHeaderContext.getClientId()).thenReturn(181L);

        long orderId = 1L;
        long productOrderDetailId = 2L;
        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long otherClientId = clientId + 1;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ClientOrder clientOrder = createClientOrder(otherClientId, 1L, 2000L,
            3000L, 500L, order);

        when(clientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(Optional.of(clientOrder));

        assertThrows(WrongClientAccessToOrder.class, () -> {
            clientOrderService.getProductOrderDetailResponseDto(headers, orderId,
                productOrderDetailId);
        });

    }


    @Test
    @DisplayName("주문 상품 옵션 상세 조회 성공")
    void getProductOrderDetailOptionResponseDtoSuccessTest() {

        long clientId = parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
        long orderId = 1L;
        long detailId = 2L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ProductOrderDetail productOrderDetail = createProductOrderDetail(order, 1L, 2L, 10000L,
            "탕비실");
        ProductOrderDetailOption productOrderDetailOption = createProductOrderDetailOption(2L,
            productOrderDetail, "포장지", 500L, 1L);

        when(productOrderDetailRepository.findById(anyLong())).thenReturn(
            Optional.of(productOrderDetail));
        when(productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
            anyLong())).thenReturn(
            Optional.ofNullable(productOrderDetailOption));

        ProductOrderDetailOptionResponseDto responseDto = clientOrderService.getProductOrderDetailOptionResponseDto(
            headers, orderId, detailId);

        assertNotNull(responseDto);

    }

    @Test
    @DisplayName("쿠폰 적용 정보 조회 테스트")
    void getCouponDiscountInfoListTest(){

        List<CouponDiscountInfoRequestDto.OrderItem> orderItemList = getOrderItemList();
        List<ProductGetNameAndPriceResponseDto> productGetNameAndPriceResponseDtoList = getProductGetNameAndPriceResponseDtoList();

        CouponDiscountInfoRequestDto requestDto = new CouponDiscountInfoRequestDto();
        ReflectionTestUtils.setField(requestDto, "orderItemList", orderItemList);
        ReflectionTestUtils.setField(requestDto, "productAndOptionTotalPrice", 180000L);

        for(int i = 0 ; i < requestDto.getOrderItemList().size(); i++){
            CouponDiscountInfoRequestDto.OrderItem item = requestDto.getOrderItemList().get(i);
            when(productClient.getSingleProductNameAndPriceSales(headers, item.getProductId())).thenReturn(ResponseEntity.ok().body(productGetNameAndPriceResponseDtoList.get(i)));
        }

        List<CouponOrderResponseDto> couponList = getCouponList();
        List<OrderCouponDiscountInfo> expectedResponse = getExpectedCouponDiscountInfoList(); // 예상결과

        when(couponClient.findClientCoupon(headers)).thenReturn(couponList);

        List<OrderCouponDiscountInfo> realResponse = clientOrderService.getCouponDiscountInfoList(headers, requestDto);

        for(int i = 0; i < realResponse.size(); i++){
            assertEquals(expectedResponse.get(i).getCouponId(), realResponse.get(i).getCouponId());
            assertEquals(expectedResponse.get(i).getIsApplicable(), realResponse.get(i).getIsApplicable());
            assertEquals(expectedResponse.get(i).getDiscountTotalAmount(), realResponse.get(i).getDiscountTotalAmount());
        }

    }

    public Order createOrder(String orderCode, Long productTotalAmount, Integer shippingFee,
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

    private List<CouponOrderResponseDto> getCouponList(){

        List<CouponOrderResponseDto> res = new ArrayList<>();

        // 금액 할인 쿠폰 - 30000원 구입 시 3000원 할인 쿠폰
        CouponPolicyDto couponPolicyDto1 = new CouponPolicyDto();
        couponPolicyDto1.setCouponPolicyDescription("최소주문금액 30000원, 3000원 할인 쿠폰");
        couponPolicyDto1.setDiscountType("AMOUNTDISCOUNT");
        couponPolicyDto1.setDiscountValue(3000L); // 3000원 의미
        couponPolicyDto1.setMinPurchaseAmount(30000L);
        couponPolicyDto1.setMaxDiscountAmount(3000L);

        CouponOrderResponseDto coupon1 = new CouponOrderResponseDto();
        coupon1.setCouponId(1L);
        coupon1.setCouponPolicyDto(couponPolicyDto1);

        res.add(coupon1);

        // 백분율 할인 쿠폰 - 40000원 이상 구입 시 10% 할인 (최대할인 5000원)쿠폰
        CouponPolicyDto couponPolicyDto2 = new CouponPolicyDto();
        couponPolicyDto2.setCouponPolicyDescription("40000원 이상 구매시 10% 할인 쿠폰 (최대할인 5000원)");
        couponPolicyDto2.setDiscountType("PERCENTAGEDISCOUNT");
        couponPolicyDto2.setDiscountValue(10); // 10% 의미
        couponPolicyDto2.setMinPurchaseAmount(40000L);
        couponPolicyDto2.setMaxDiscountAmount(5000L);

        CouponOrderResponseDto coupon2 = new CouponOrderResponseDto();
        coupon2.setCouponId(2L);
        coupon2.setCouponPolicyDto(couponPolicyDto2);

        res.add(coupon2);

        // 상품 금액 할인 쿠폰
        CouponPolicyDto couponPolicyDto3 = new CouponPolicyDto();
        couponPolicyDto3.setCouponPolicyDescription("상품아이디 1L을 구매하면, 해당 상품 30000원 이상 구매시 3000원 할인 쿠폰");
        couponPolicyDto3.setDiscountType("AMOUNTDISCOUNT");
        couponPolicyDto3.setDiscountValue(3000L); // 3000원 의미
        couponPolicyDto3.setMinPurchaseAmount(30000L);
        couponPolicyDto3.setMaxDiscountAmount(3000L);

        ProductCoupon productCoupon1  = new ProductCoupon();
        productCoupon1.setProductId(1L);

        CouponOrderResponseDto coupon3 = new CouponOrderResponseDto();
        coupon3.setCouponId(3L);
        coupon3.setCouponPolicyDto(couponPolicyDto3);
        coupon3.setProductCoupon(productCoupon1);

        res.add(coupon3);

        // 상품 백분율 할인 쿠폰
        CouponPolicyDto couponPolicyDto4 = new CouponPolicyDto();
        couponPolicyDto4.setCouponPolicyDescription("상품아이디 1L을 구매하면, 해당 상품 40000원 이상 구매시 10% 할인 쿠폰 (최대 할인 5000원)");
        couponPolicyDto4.setDiscountType("PERCENTAGEDISCOUNT");
        couponPolicyDto4.setDiscountValue(10); // 10% 의미
        couponPolicyDto4.setMinPurchaseAmount(40000L);
        couponPolicyDto4.setMaxDiscountAmount(5000L);

        ProductCoupon productCoupon2  = new ProductCoupon();
        productCoupon2.setProductId(1L);

        CouponOrderResponseDto coupon4 = new CouponOrderResponseDto();
        coupon4.setCouponId(4L);
        coupon4.setCouponPolicyDto(couponPolicyDto4);
        coupon4.setProductCoupon(productCoupon2);

        res.add(coupon4);

        // 카테고리 금액 할인 쿠폰
        CouponPolicyDto couponPolicyDto5 = new CouponPolicyDto();
        couponPolicyDto5.setCouponPolicyDescription("카테고리 1L 상품을 구매하면, 해당 카테고리 상품의 총 주문 금액이 30000원 이상 구매시 3000원 할인 쿠폰");
        couponPolicyDto5.setDiscountType("AMOUNTDISCOUNT");
        couponPolicyDto5.setDiscountValue(3000L); // 3000원 의미
        couponPolicyDto5.setMinPurchaseAmount(30000L);
        couponPolicyDto5.setMaxDiscountAmount(3000L);

        CategoryCoupon categoryCoupon1  = new CategoryCoupon();
        categoryCoupon1.setProductCategoryId(1L);

        CouponOrderResponseDto coupon5 = new CouponOrderResponseDto();
        coupon5.setCouponId(5L);
        coupon5.setCouponPolicyDto(couponPolicyDto5);
        coupon5.setCategoryCoupon(categoryCoupon1);

        res.add(coupon5);

        // 카테고리 백분율 할인 쿠폰
        CouponPolicyDto couponPolicyDto6 = new CouponPolicyDto();
        couponPolicyDto6.setCouponPolicyDescription("카테고리 1L 상품을 구매하면, 해당 카테고리 상품의 총 주문 금액이 40000원 이상 구매시 10% 할인 쿠폰 (최대 할인 5000원)");
        couponPolicyDto6.setDiscountType("PERCENTAGEDISCOUNT");
        couponPolicyDto6.setDiscountValue(10); // 10% 의미
        couponPolicyDto6.setMinPurchaseAmount(40000L);
        couponPolicyDto6.setMaxDiscountAmount(5000L);

        CategoryCoupon categoryCoupon2  = new CategoryCoupon();
        categoryCoupon2.setProductCategoryId(1L);

        CouponOrderResponseDto coupon6 = new CouponOrderResponseDto();
        coupon6.setCouponId(6L);
        coupon6.setCouponPolicyDto(couponPolicyDto6);
        coupon6.setCategoryCoupon(categoryCoupon2);

        res.add(coupon6);

        return res;

    }

    private List<OrderCouponDiscountInfo> getExpectedCouponDiscountInfoList() {

        List<OrderCouponDiscountInfo> res = new ArrayList<>();

        // 1L 쿠폰 결과 예상
        OrderCouponDiscountInfo discountInfo1 = new OrderCouponDiscountInfo();
        ReflectionTestUtils.setField(discountInfo1, "couponId", 1L);
        ReflectionTestUtils.setField(discountInfo1, "isApplicable", true);
        ReflectionTestUtils.setField(discountInfo1, "discountTotalAmount", 3000L);
        res.add(discountInfo1);


        // 2L 쿠폰 결과 예상
        OrderCouponDiscountInfo discountInfo2 = new OrderCouponDiscountInfo();
        ReflectionTestUtils.setField(discountInfo2, "couponId", 2L);
        ReflectionTestUtils.setField(discountInfo2, "isApplicable", true);
        ReflectionTestUtils.setField(discountInfo2, "discountTotalAmount", 5000L);
        res.add(discountInfo2);


        // 3L 쿠폰 결과 예상
        OrderCouponDiscountInfo discountInfo3 = new OrderCouponDiscountInfo();
        ReflectionTestUtils.setField(discountInfo3, "couponId", 3L);
        ReflectionTestUtils.setField(discountInfo3, "isApplicable", true);
        ReflectionTestUtils.setField(discountInfo3, "discountTotalAmount", 3000L);
        res.add(discountInfo3);


        // 4L 쿠폰 결과 예상
        OrderCouponDiscountInfo discountInfo4 = new OrderCouponDiscountInfo();
        ReflectionTestUtils.setField(discountInfo4, "couponId", 4L);
        ReflectionTestUtils.setField(discountInfo4, "isApplicable", false);
        ReflectionTestUtils.setField(discountInfo4, "discountTotalAmount", null);
        res.add(discountInfo4);


        // 5L 쿠폰 결과 예상
        OrderCouponDiscountInfo discountInfo5 = new OrderCouponDiscountInfo();
        ReflectionTestUtils.setField(discountInfo5, "couponId", 5L);
        ReflectionTestUtils.setField(discountInfo5, "isApplicable", true);
        ReflectionTestUtils.setField(discountInfo5, "discountTotalAmount", 3000L);
        res.add(discountInfo5);


        // 6L 쿠폰 결과 예상
        OrderCouponDiscountInfo discountInfo6 = new OrderCouponDiscountInfo();
        ReflectionTestUtils.setField(discountInfo6, "couponId", 6L);
        ReflectionTestUtils.setField(discountInfo6, "isApplicable", true);
        ReflectionTestUtils.setField(discountInfo6, "discountTotalAmount", 5000L);
        res.add(discountInfo6);


        return res;
    }

    private List<CouponDiscountInfoRequestDto.OrderItem> getOrderItemList(){


        // 첫번째 주문 상품
        List<Long> categoryIdList1 = new ArrayList<>(List.of(2L, 3L));
        CouponDiscountInfoRequestDto.OrderItem orderItem1 = new CouponDiscountInfoRequestDto.OrderItem();
        ReflectionTestUtils.setField(orderItem1, "productId", 1L);
        ReflectionTestUtils.setField(orderItem1, "quantity", 3L);
        ReflectionTestUtils.setField(orderItem1, "categoryIdList", categoryIdList1);

        // 두번째 주문 상품
        List<Long> categoryIdList2 = new ArrayList<>(List.of(1L, 3L));
        CouponDiscountInfoRequestDto.OrderItem orderItem2 = new CouponDiscountInfoRequestDto.OrderItem();
        ReflectionTestUtils.setField(orderItem2, "productId", 2L);
        ReflectionTestUtils.setField(orderItem2, "quantity", 2L);
        ReflectionTestUtils.setField(orderItem2, "categoryIdList", categoryIdList2);

        // 세번째 주문 상품
        List<Long> categoryIdList3 = new ArrayList<>(List.of(1L, 4L));
        CouponDiscountInfoRequestDto.OrderItem orderItem3 = new CouponDiscountInfoRequestDto.OrderItem();
        ReflectionTestUtils.setField(orderItem3, "productId", 3L);
        ReflectionTestUtils.setField(orderItem3, "quantity", 4L);
        ReflectionTestUtils.setField(orderItem3, "categoryIdList", categoryIdList3);

        // 네번째 주문 상품
        List<Long> categoryIdList4 = new ArrayList<>(List.of(2L));
        CouponDiscountInfoRequestDto.OrderItem orderItem4 = new CouponDiscountInfoRequestDto.OrderItem();
        ReflectionTestUtils.setField(orderItem4, "productId", 4L);
        ReflectionTestUtils.setField(orderItem4, "quantity", 1L);
        ReflectionTestUtils.setField(orderItem4, "categoryIdList", categoryIdList4);

        return new ArrayList<>(List.of(orderItem1, orderItem2, orderItem3, orderItem4));
    }

    private List<ProductGetNameAndPriceResponseDto> getProductGetNameAndPriceResponseDtoList(){
        ProductGetNameAndPriceResponseDto responseDto1 = ProductGetNameAndPriceResponseDto.builder()
            .productId(1L)
            .productName("주문상품1")
            .productPriceSales(10000L)
            .build();
        ProductGetNameAndPriceResponseDto responseDto2 = ProductGetNameAndPriceResponseDto.builder()
            .productId(2L)
            .productName("주문상품2")
            .productPriceSales(15000L)
            .build();
        ProductGetNameAndPriceResponseDto responseDto3 = ProductGetNameAndPriceResponseDto.builder()
            .productId(3L)
            .productName("주문상품3")
            .productPriceSales(15000L)
            .build();
        ProductGetNameAndPriceResponseDto responseDto4 = ProductGetNameAndPriceResponseDto.builder()
            .productId(4L)
            .productName("주문상품4")
            .productPriceSales(20000L)
            .build();
        return new ArrayList<>(List.of(responseDto1, responseDto2, responseDto3, responseDto4));
    }

}
