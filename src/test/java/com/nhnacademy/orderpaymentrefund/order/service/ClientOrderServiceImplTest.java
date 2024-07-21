package com.nhnacademy.orderpaymentrefund.order.service;

import com.nhnacademy.orderpaymentrefund.converter.impl.ClientOrderConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.impl.ClientOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;

@ExtendWith(MockitoExtension.class)
class ClientOrderServiceImplTest {

    private static final String ID_HEADER = "X-User-Id";

    @Mock
    private ProductOrderDetailRepository productOrderDetailRepository;
    @Mock
    private ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Spy
    private ClientOrderConverterImpl clientOrderConverter;
    private ProductOrderDetailConverter productOrderDetailConverter;
    private ProductOrderDetailOptionConverter productOrderDetailOptionConverter;

    @InjectMocks
    private ClientOrderServiceImpl clientOrderServiceImpl;


    HttpHeaders headers;
    String tossOrderId;

    @BeforeEach
    void setUp(){
        headers = new HttpHeaders();
        headers.add(ID_HEADER, "181");
        tossOrderId = "uuid-1234";
    }

}
