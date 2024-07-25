//package com.nhnacademy.orderpaymentrefund.service.order;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.nhnacademy.orderpaymentrefund.domain.order.Order;
//import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
//import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
//import com.nhnacademy.orderpaymentrefund.service.order.impl.SchedulingServiceImpl;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//@ExtendWith(MockitoExtension.class)
//class SchedulingServiceImplTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @InjectMocks
//    private SchedulingServiceImpl schedulingService;
//
//    @Captor
//    private ArgumentCaptor<LocalDateTime> dateTimeCaptor;
//
//    private List<Order> orderList;
//
//    @BeforeEach
//    public void setUp() {
//        // Set up test data and mock behavior here
//        LocalDateTime currentOrderDateTime = LocalDateTime.of(2024, 5, 5, 12, 0);
//
//        Order order1 = createOrder("uuid-1", 10000L, 3000, null, "01012341234", "광주 광역시 어쩌구 저쩌구");
//        Order order2 = createOrder("uuid-2", 40000L, 0, null, "01012341234", "광주 광역시 어쩌구 저쩌구");
//
//        ReflectionTestUtils.setField(order1, "orderStatus", OrderStatus.DELIVERING);
//        ReflectionTestUtils.setField(order2, "orderStatus", OrderStatus.DELIVERING);
//
//        ReflectionTestUtils.setField(order1, "orderDatetime", currentOrderDateTime);
//        ReflectionTestUtils.setField(order2, "orderDatetime", currentOrderDateTime);
//
//        orderList = List.of(order1, order2);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime thresholdDateTime = currentOrderDateTime.plusMinutes(10);
//        String formattedDateTime = thresholdDateTime.format(formatter);
//        LocalDateTime parsedDateTime = LocalDateTime.parse(formattedDateTime, formatter);
//
//        when(orderRepository.findAllByOrderStatusAndOrderDatetimeBefore(eq(OrderStatus.DELIVERING), dateTimeCaptor.capture()))
//            .thenReturn(orderList);
//    }
//
//    @Test
//    public void testScheduleOrderStatusToDeliveryCompleted() {
//        schedulingService.scheduleOrderStatusToDeliveryCompleted();
//
//        LocalDateTime capturedDateTime = dateTimeCaptor.getValue();
//        LocalDateTime expectedDateTime = LocalDateTime.now().minusMinutes(10);
//
//        // Verify that the captured date time is close to the expected date time
//        assertEquals(expectedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
//            capturedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
//
//        for (Order order : orderList) {
//            verify(order).updateOrderStatus(OrderStatus.DELIVERY_COMPLETE);
//            verify(order).updateDeliveryStartDate();
//            verify(orderRepository).save(order);
//        }
//    }
//
//
//    public Order createOrder(String orderCode, Long productTotalAmount, Integer shippingFee,
//        LocalDate designatedDeliveryDate,
//        String phoneNumber, String deliveryAddress) {
//        return Order.builder()
//            .orderCode(orderCode)
//            .productTotalAmount(productTotalAmount)
//            .shippingFee(shippingFee)
//            .designatedDeliveryDate(designatedDeliveryDate)
//            .phoneNumber(phoneNumber)
//            .deliveryAddress(deliveryAddress)
//            .build();
//    }
//
//}
