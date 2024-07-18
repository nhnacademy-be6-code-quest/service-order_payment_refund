package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.service.order.SchedulingService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {

    private final OrderRepository orderRepository;

    @Scheduled(fixedRate = 5000) // 매 5초마다 실행
    public void scheduleOrderStatusToDeliveryCompleted() {

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        List<Order> orderList = orderRepository.findAllByOrderStatusAndOrderDatetimeBefore(OrderStatus.DELIVERING, threshold);

        for(Order order : orderList){
            order.updateOrderStatus(OrderStatus.DELIVERY_COMPLETE);
            order.updateDeliveryStartDate();
            orderRepository.save(order);
        }

    }

}
