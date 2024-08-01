package com.nhnacademy.orderpaymentrefund.service.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.service.order.impl.SchedulingServiceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private SchedulingServiceImpl schedulingService;

    @Captor
    private ArgumentCaptor<LocalDateTime> dateTimeCaptor;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Test
    void testScheduleOrderStatusToDeliveryCompleted() {

        LocalDateTime threshold = LocalDateTime.of(2024, 5, 5, 12, 10);

        Order order = createOrder("uuid-1", 10000L, 3000, null, "01012341234", "광주 광역시 어쩌구 저쩌구");

        ReflectionTestUtils.setField(order, "orderStatus", OrderStatus.DELIVERING);

        ReflectionTestUtils.setField(order, "orderDatetime", threshold);

        List<Order> orderList = List.of(order);


        // Mock repository behavior
        when(orderRepository.findAllByOrderStatusAndOrderDatetimeBefore(eq(OrderStatus.DELIVERING), any(LocalDateTime.class)))
            .thenReturn(orderList);

        // Call the method under test
        schedulingService.scheduleOrderStatusToDeliveryCompleted();

        // Check if the orders' statuses have been updated correctly
        assertNotEquals(OrderStatus.DELIVERING, order.getOrderStatus());

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
}