package com.nhnacademy.orderpaymentrefund.service.order;

import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.NonClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    private static final String ID_HEADER = "X-User-Id";

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

    HttpHeaders headers;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.set(ID_HEADER, "181");
    }

    @Test
    @DisplayName("")
    void getPaymentOrderShowRequestDtoTest() {

    }

    @Test
    @DisplayName("회원 주문 임시 저장")
    void getPaymentOrderApproveRequestDtoTest() {

    }

    @Test
    @DisplayName()
    void changeOrderStatusTest(){

    }

    @Test
    @DisplayName()
    void getAllOrderListTest(){

    }

    @Test
    @DisplayName()
    void getProductOrderDetailListTest(){

    }

    @Test
    @DisplayName()
    void getProductOrderDetailTest(){

    }

    @Test
    @DisplayName()
    void getProductOrderDetailOptionResponseDtoTest(){

    }




}
