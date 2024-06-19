package com.nhnacademy.order.service.impl;

import com.nhnacademy.order.domain.order.Order;
import com.nhnacademy.order.domain.order.OrderDetail;
import com.nhnacademy.order.domain.shipping.ShippingPolicy;
import com.nhnacademy.order.dto.order.request.system.SystemOrderPostRequestDto;
import com.nhnacademy.order.dto.order.request.system.SystemOrderPutRequestDto;
import com.nhnacademy.order.exception.OrderNotFoundException;
import com.nhnacademy.order.exception.ShippingPolicyNotFoundException;
import com.nhnacademy.order.repository.OrderDetailRepository;
import com.nhnacademy.order.repository.OrderRepository;
import com.nhnacademy.order.repository.ShippingPolicyRepository;
import com.nhnacademy.order.service.SystemOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SystemOrderServiceImpl implements SystemOrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private ShippingPolicyRepository shippingPolicyRepository;

    @Override
    public void createOrder(SystemOrderPostRequestDto orderPostRequestDto) {

        // 배송 정책 가져오기
        ShippingPolicy shippingPolicy = shippingPolicyRepository.findById(orderPostRequestDto.getShippingPolicyId()).orElseThrow(()->new ShippingPolicyNotFoundException());

        // order 생성 및 저장
        Order order = Order.builder()
                .orderDate(orderPostRequestDto.getOrderDate())
                .deliveryDate(orderPostRequestDto.getDeliveryDate())
                .orderStatus(orderPostRequestDto.getOrderStatus())
                .totalPrice(orderPostRequestDto.getTotalPrice())
                .clientId(orderPostRequestDto.getClientId())
                .shippingPolicy(shippingPolicy)
                .shippingFee(orderPostRequestDto.getShippingFee())
                .build();
        Order savedOrder = orderRepository.save(order);

        // OrderDetail 생성
        orderPostRequestDto.getOrderDetailDtoList().forEach(orderDetailDto -> {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(savedOrder)
                    .quantity(orderDetailDto.getQuantity())
                    .productPrice(orderDetailDto.getPrice())
                    .productId(orderDetailDto.getProductId())
                    .build();
            orderDetailRepository.save(orderDetail);
        });

    }

    @Override
    public void updateOrder(long orderId, SystemOrderPutRequestDto orderPutRequestDto) {
        Order savedOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException());
        Order updatedOrder = Order.builder()
                .orderDate(savedOrder.getOrderDate())
                .deliveryDate(savedOrder.getDeliveryDate())
                .orderStatus(orderPutRequestDto.getOrderStatus())
                .totalPrice(savedOrder.getTotalPrice())
                .clientId(savedOrder.getClientId())
                .shippingPolicy(savedOrder.getShippingPolicy())
                .shippingFee(savedOrder.getShippingFee())
                .build();
        orderRepository.save(updatedOrder);
    }
}
