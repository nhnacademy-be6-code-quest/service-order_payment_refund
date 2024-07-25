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
class OrderServiceImplTest {

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
    @DisplayName("비회원 임시 저장 테스트")
    void getPaymentOrderShowRequestDtoTest() {

    }

    @Test
    @DisplayName("비회원 임시 저장 주문 조회 테스트")
    void getPaymentOrderApproveRequestDtoTest() {

    }

    @Test
    @DisplayName("비회원 주문 아이디 목록 페이지 조회")
    void changeOrderStatusTest(){

    }

    @Test
    @DisplayName("비회원 주문 비밀번호 조회")
    void getAllOrderListTest(){

    }

    @Test
    @DisplayName("비회원 주문 단건 조회")
    void getProductOrderDetailListTest(){

    }

    @Test
    @DisplayName("주문 취소 테스트")
    void getProductOrderDetailTest(){

    }

    @Test
    @DisplayName("")
    void getProductOrderDetailOptionResponseDtoTest(){

    }

}
