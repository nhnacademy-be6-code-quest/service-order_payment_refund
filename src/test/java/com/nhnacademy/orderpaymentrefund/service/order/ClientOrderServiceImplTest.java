//package com.nhnacademy.orderpaymentrefund.service.order;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
//import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
//import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
//import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
//import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
//import com.nhnacademy.orderpaymentrefund.service.order.impl.ClientOrderServiceImpl;
//import java.lang.reflect.Field;
//import java.time.LocalDate;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpHeaders;
//
//@ExtendWith(MockitoExtension.class)
//class ClientOrderServiceImplTest {
//
//    private static final String ID_HEADER = "X-User-Id";
//
//    @Mock
//    ProductOrderDetailRepository productOrderDetailRepository;
//    @Mock
//    ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
//    @Mock
//    OrderRepository orderRepository;
//    @Mock
//    RedisTemplate<String, Object> redisTemplate;
//    @Mock
//    HashOperations<String, Object, Object> hashOperations;
//
//    @InjectMocks
//    ClientOrderServiceImpl clientOrderService;
//
//    HttpHeaders headers;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
//        headers = new HttpHeaders();
//        headers.set(ID_HEADER, "181");
//    }
//
//    @Test
//    @DisplayName("회원 주문 임시 저장")
//    void testSaveClientTemporalOrder() throws NoSuchFieldException, IllegalAccessException {
//
//        ClientOrderCreateForm requestDto = new ClientOrderCreateForm();
//        setFieldValue(requestDto, "orderDetailDtoItemList", List.of(new OrderDetailDtoItem()));
//        setFieldValue(requestDto, "couponId", 123L);
//        setFieldValue(requestDto, "shippingFee", 5000);
//        setFieldValue(requestDto, "productTotalAmount", 20000L);
//        setFieldValue(requestDto, "orderTotalAmount", 25000L);
//        setFieldValue(requestDto, "payAmount", 25000L);
//        setFieldValue(requestDto, "couponDiscountAmount", 0L);
//        setFieldValue(requestDto, "usedPointDiscountAmount", 0L);
//        setFieldValue(requestDto, "orderedPersonName", "홍길동");
//        setFieldValue(requestDto, "phoneNumber", "01012345678");
//        setFieldValue(requestDto, "deliveryAddress", "123 Main St");
//        setFieldValue(requestDto, "useDesignatedDeliveryDate", true);
//        setFieldValue(requestDto, "designatedDeliveryDate", LocalDate.of(2024, 8, 1));
//        setFieldValue(requestDto, "paymentMethod", 1);
//        setFieldValue(requestDto, "accumulatePoint", 100L);
//        setFieldValue(requestDto, "tossOrderId", "uuid-1234");
//
//        clientOrderService.saveClientTemporalOrder(headers, requestDto);
//
//        verify(redisTemplate.opsForHash(), times(1)).put(eq("order"), anyString(), eq(requestDto));
//
//    }
//
////    @Test
////    @DisplayName("회원 주문 임시 데이터 가져오기")
////    void testGetClientTemporalOrder() throws Exception {
////        // Given
////        ClientOrderCreateForm expectedDto = new ClientOrderCreateForm();
////        setFieldValue(expectedDto, "orderDetailDtoItemList", List.of(new OrderDetailDtoItem(/* Initialize with necessary data */)));
////        setFieldValue(expectedDto, "couponId", 1L);
////        setFieldValue(expectedDto, "shippingFee", 5000);
////        setFieldValue(expectedDto, "productTotalAmount", 20000L);
////        setFieldValue(expectedDto, "orderTotalAmount", 25000L);
////        setFieldValue(expectedDto, "payAmount", 25000L);
////        setFieldValue(expectedDto, "couponDiscountAmount", 1000L);
////        setFieldValue(expectedDto, "usedPointDiscountAmount", 2000L);
////        setFieldValue(expectedDto, "orderedPersonName", "홍길동");
////        setFieldValue(expectedDto, "phoneNumber", "01012345678");
////        setFieldValue(expectedDto, "deliveryAddress", "전라남도 순천시 시청");
////        setFieldValue(expectedDto, "useDesignatedDeliveryDate", true);
////        setFieldValue(expectedDto, "designatedDeliveryDate", LocalDate.of(2024, 8, 1));
////        setFieldValue(expectedDto, "paymentMethod", 1);
////        setFieldValue(expectedDto, "accumulatePoint", 500L);
////        setFieldValue(expectedDto, "tossOrderId", "uuid-1234");
////
////
////        when(redisTemplate.opsForHash().get("order", "uuid-1234")).thenReturn(expectedDto);
////
////        // When
////        ClientOrderCreateForm actualDto = clientOrderService.getClientTemporalOrder(headers, "uuid-1234");
////
////        // Then
////        assertEquals(expectedDto, actualDto);
////        verify(hashOperations, times(1)).get("order", "uuid-1234");
////    }
//
////    @Test
////    @DisplayName("회원의 모든 주문 조회")
////
////    @Test
////    @DisplayName("회원 임시 주문 조회")
//
//
//
//    // 리플렉션으로 값 주입
//    private void setFieldValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
//        Field field = obj.getClass().getDeclaredField(fieldName);
//        field.setAccessible(true); // Allow access to private fields
//        field.set(obj, value);
//    }
//
//
//
//
//
//}
