package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.dto.order.request.system.SystemOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.system.SystemOrderPutRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.ShippingPolicyNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.shipping.ShippingPolicyRepository;
import com.nhnacademy.orderpaymentrefund.service.order.SystemOrderService;
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

        // OrderDetail 생성 및 저장
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
