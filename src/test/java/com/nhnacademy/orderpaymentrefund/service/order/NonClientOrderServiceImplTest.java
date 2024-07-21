package com.nhnacademy.orderpaymentrefund.service.order;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.impl.NonClientOrderServiceImpl;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;

@ExtendWith(MockitoExtension.class)
class NonClientOrderServiceImplTest {

    private static final String ID_HEADER = "X-User-Id";

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private NonClientOrderServiceImpl nonClientOrderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    // 리플렉션으로 값 주입
    private void setFieldValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Allow access to private fields
        field.set(obj, value);
    }

//    @Test
//    @DisplayName("비회원 주문 임시 저장")
//    void testSaveNonClientTemporalOrder() throws Exception {
//        NonClientOrderForm requestDto = new NonClientOrderForm();
//
//        setFieldValue(requestDto, "orderDetailDtoItemList", List.of(new OrderDetailDtoItem(/* Initialize with necessary data */)));
//        setFieldValue(requestDto, "shippingFee", 5000);
//        setFieldValue(requestDto, "productTotalAmount", 20000L);
//        setFieldValue(requestDto, "payAmount", 25000L);
//        setFieldValue(requestDto, "orderedPersonName", "홍길동");
//        setFieldValue(requestDto, "email", "test@test.com");
//        setFieldValue(requestDto, "phoneNumber", "01012345678");
//        setFieldValue(requestDto, "addressZipCode", "12345");
//        setFieldValue(requestDto, "deliveryAddress", "전라남도 순천시 시청");
//        setFieldValue(requestDto, "deliveryDetailAddress", "1층 안내소");
//        setFieldValue(requestDto, "useDesignatedDeliveryDate", true);
//        setFieldValue(requestDto, "designatedDeliveryDate", LocalDate.of(2024, 8, 1));
//        setFieldValue(requestDto, "paymentMethod", 1);
//        setFieldValue(requestDto, "orderPassword", "1234");
//        setFieldValue(requestDto, "tossOrderId", "uuid-1234");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-User-Id", "test-user-id");
//
//        nonClientOrderService.saveNonClientTemporalOrder(headers, requestDto);
//
//        verify(hashOperations, times(1)).put(eq("order"), eq("uuid-1234"), eq(requestDto));
//    }

}
