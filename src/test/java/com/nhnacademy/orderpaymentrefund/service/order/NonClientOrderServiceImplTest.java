package com.nhnacademy.orderpaymentrefund.service.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.UpdateNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.repository.order.NonClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.impl.NonClientOrderServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class NonClientOrderServiceImplTest {

    private static final String REDIS_KEY = "order";

    @Mock
    private ClientHeaderContext clientHeaderContext;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private NonClientOrderRepository nonClientOrderRepository;
    @Mock
    private ProductOrderDetailRepository productOrderDetailRepository;
    @Mock
    private ProductOrderDetailOptionRepository productOrderDetailOptionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private NonClientOrderServiceImpl nonClientOrderService;

    HttpHeaders headers;

    @BeforeEach
    public void setUp() {
        headers = new HttpHeaders();
        when(clientHeaderContext.isClient()).thenReturn(false);
    }

    @Test
    @DisplayName("비회원 주문 임시 저장")
    void testSaveNonClientTemporalOrder() {

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        String orderCode = "uuid-1234";

        NonClientOrderForm requestDto = new NonClientOrderForm();
        ReflectionTestUtils.setField(requestDto, "orderCode", orderCode);

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        doNothing().when(hashOperations)
            .put(eq(REDIS_KEY), anyString(), any(NonClientOrderForm.class));

        nonClientOrderService.saveNonClientTemporalOrder(headers, requestDto);

        verify(hashOperations, times(1)).put(REDIS_KEY, "uuid-1234", requestDto);

    }


    @Test
    @DisplayName("비회원 임시 저장 주문 조회")
    void getNonClientTemporalOrderTest() {

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        String orderCode = "uuid-1234";

        NonClientOrderForm expectedDto = new NonClientOrderForm();
        ReflectionTestUtils.setField(expectedDto, "orderCode", orderCode);

        when(hashOperations.get(REDIS_KEY, orderCode)).thenReturn(expectedDto);

        NonClientOrderForm actualDto = nonClientOrderService.getNonClientTemporalOrder(headers,
            orderCode);

        assertEquals(expectedDto, actualDto);

        verify(hashOperations, times(1)).get(REDIS_KEY, orderCode);

    }


    @Test
    @DisplayName("비회원 주문 아이디 조회 페이지")
    void findNonClientOrderIdTest() {

        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "정렬 필드";
        Sort sort = Sort.by(sortBy).ascending();

        String ordererName = "홍길동";
        String phoneNumber = "01012341234";
        String email = "test@email.com";
        String password = "1234";

        Long orderId1 = 1L;
        Long orderId2 = 2L;

        FindNonClientOrderIdRequestDto requestDto = FindNonClientOrderIdRequestDto.builder()
            .ordererName(ordererName)
            .phoneNumber(phoneNumber)
            .email(email)
            .build();

        Order order1 = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order1, "orderId", orderId1);
        NonClientOrder nonClientOrder1 = createNonClientOrder(password, ordererName, email, order1);
        ReflectionTestUtils.setField(nonClientOrder1, "order", order1);

        Order order2 = createOrder("uuid-4321", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order2, "orderId", orderId2);
        NonClientOrder nonClientOrder2 = createNonClientOrder(password, ordererName, email, order2);
        ReflectionTestUtils.setField(nonClientOrder2, "order", order2);

        List<NonClientOrder> nonClientOrderList = new ArrayList<>(
            List.of(nonClientOrder1, nonClientOrder2));

        when(
            nonClientOrderRepository.findRecent10OrderNonClientOrder(
                requestDto.ordererName(), requestDto.email(), requestDto.phoneNumber())
        ).thenReturn(nonClientOrderList);

        List<FindNonClientOrderIdInfoResponseDto> res = nonClientOrderService.findNonClientOrderId(
            headers, requestDto);

        assertNotNull(res);

    }

    @Test
    @DisplayName("비회원 단건 주문 조회 - 성공")
    void getOrderTest() {

        Long orderId = 1L;
        String ordererName = "홍길동";
        String phoneNumber = "01012341234";
        String email = "test@test.com";
        String password = "1234";

        String requestPassword = "1234";
        Long requestOrderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            phoneNumber, "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order, "orderId", orderId);
        NonClientOrder nonClientOrder = createNonClientOrder(password, ordererName, email, order);
        ReflectionTestUtils.setField(nonClientOrder, "order", order);

        ProductOrderDetail productOrderDetail1 = createProductOrderDetail(order, 1L, 2L, 10000L,
            "상품1");
        ReflectionTestUtils.setField(productOrderDetail1, "productId", 1L);
        ProductOrderDetail productOrderDetail2 = createProductOrderDetail(order, 2L, 1L, 10000L,
            "상품2");
        ReflectionTestUtils.setField(productOrderDetail2, "productId", 2L);

        when(
            nonClientOrderRepository.findByOrder_OrderId(requestOrderId)
        ).thenReturn(Optional.of(nonClientOrder));

        when(
            passwordEncoder.matches(requestPassword, password)
        ).thenReturn(true);

        List<ProductOrderDetail> productOrderDetailList = new ArrayList<>(
            List.of(productOrderDetail1, productOrderDetail2));

        List<ProductOrderDetailOption> productOrderDetailOptionList = new ArrayList<>();

        for (int i = 0; i < productOrderDetailList.size(); i++) {

            ProductOrderDetail productOrderDetail = productOrderDetailList.get(i);
            ProductOrderDetailOption productOrderDetailOption = createProductOrderDetailOption(i,
                productOrderDetail, "곰돌이 포장지", 500L, 1L);

            productOrderDetailOptionList.add(productOrderDetailOption);

        }

        when(
            productOrderDetailRepository.findAllByOrder_OrderId(requestOrderId)
        ).thenReturn(productOrderDetailList);

        for (int i = 0; i < productOrderDetailList.size(); i++) {
            ProductOrderDetail productOrderDetail = productOrderDetailList.get(i);
            when(
                productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
                    productOrderDetail.getProductOrderDetailId())
            ).thenReturn(
                Optional.ofNullable(productOrderDetailOptionList.get(i))
            );
        }

        NonClientOrderGetResponseDto responseDto = nonClientOrderService.getOrder(headers,
            requestOrderId, requestPassword);

        assertNotNull(responseDto);

    }

    @Test
    @DisplayName("비회원 단건 주문 조회 - 성공 (option이 null일때)")
    void getOrderTest2() {

        Long orderId = 1L;
        String ordererName = "홍길동";
        String phoneNumber = "01012341234";
        String email = "test@test.com";
        String password = "1234";

        String requestPassword = "1234";
        Long requestOrderId = 1L;

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            phoneNumber, "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order, "orderId", orderId);
        NonClientOrder nonClientOrder = createNonClientOrder(password, ordererName, email, order);
        ReflectionTestUtils.setField(nonClientOrder, "order", order);

        ProductOrderDetail productOrderDetail1 = createProductOrderDetail(order, 1L, 2L, 10000L,
            "상품1");
        ReflectionTestUtils.setField(productOrderDetail1, "productOrderDetailId", 1L);
        ProductOrderDetail productOrderDetail2 = createProductOrderDetail(order, 2L, 1L, 10000L,
            "상품2");
        ReflectionTestUtils.setField(productOrderDetail2, "productOrderDetailId", 2L);

        when(
            nonClientOrderRepository.findByOrder_OrderId(requestOrderId)
        ).thenReturn(Optional.of(nonClientOrder));

        when(
            passwordEncoder.matches(requestPassword, password)
        ).thenReturn(true);

        List<ProductOrderDetail> productOrderDetailList = new ArrayList<>(
            List.of(productOrderDetail1, productOrderDetail2));

        when(
            productOrderDetailRepository.findAllByOrder_OrderId(requestOrderId)
        ).thenReturn(productOrderDetailList);

        for (ProductOrderDetail productOrderDetail : productOrderDetailList) {
            when(
                productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
                    productOrderDetail.getProductOrderDetailId())
            ).thenReturn(
                Optional.empty()
            );
        }

        NonClientOrderGetResponseDto responseDto = nonClientOrderService.getOrder(headers,
            requestOrderId, requestPassword);

        assertNotNull(responseDto);

    }

    @Test
    @DisplayName("비회원 주문 비밀번호 변경")
    void updateNonClientOrderPasswordTest() {
        long orderId = 1L;
        String ordererName = "홍길동";
        String phoneNumber = "01012341234";
        String email = "test@test.com";
        String newPassword = "newPassword";
        String password = "1234";

        UpdateNonClientOrderPasswordRequestDto requestDto = new UpdateNonClientOrderPasswordRequestDto();
        ReflectionTestUtils.setField(requestDto, "ordererName", ordererName);
        ReflectionTestUtils.setField(requestDto, "phoneNumber", phoneNumber);
        ReflectionTestUtils.setField(requestDto, "email", email);
        ReflectionTestUtils.setField(requestDto, "newPassword", newPassword);

        Order order = createOrder("uuid-1234", 10000L, 2000, LocalDate.now(),
            "01012341234", "전라남도 광주시 랄랄랄라");
        ReflectionTestUtils.setField(order, "orderId", orderId);
        NonClientOrder nonClientOrder = createNonClientOrder(password, ordererName, email, order);
        ReflectionTestUtils.setField(nonClientOrder, "order", order);

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        when(nonClientOrderRepository.findByOrder_OrderId(orderId)).thenReturn(
            Optional.of(nonClientOrder));

        nonClientOrderService.updateNonClientOrderPassword(headers, orderId, requestDto);

        assertEquals("encodedPassword", nonClientOrder.getNonClientOrderPassword());

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

}
