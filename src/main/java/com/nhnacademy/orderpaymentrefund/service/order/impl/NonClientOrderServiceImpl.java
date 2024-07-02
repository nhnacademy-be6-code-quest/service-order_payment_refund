package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.service.order.CommonOrderService;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class NonClientOrderServiceImpl implements NonClientOrderService {

    private OrderRepository orderRepository;
    private CommonOrderService commonOrderService;

    @Override
    public URI tryCreateOrder(CreateNonClientOrderRequestDto requestDto) {
        preprocessing();
        createOrder(requestDto);
        postprocessing();
        return null;
    }

    @Override
    public void preprocessing() {

    }

    @Override
    public void postprocessing() {

    }

    @Override
    public void createOrder(CreateNonClientOrderRequestDto requestDto) {

        // 배송 주소
        StringBuilder address = new StringBuilder();
        address.append(requestDto.nonClientOrdererInfoDto().zipCode());
        address.append(", ");
        address.append(requestDto.nonClientOrdererInfoDto().address());
        address.append(", ");
        address.append(requestDto.nonClientOrdererInfoDto().detailAddress());

        // 비회원 Order 생성
        Order order = Order.nonClientOrderBuilder()
                .tossOrderId(UUID.randomUUID().toString())
                .productTotalAmount(requestDto.nonClientOrderPriceInfoDto().productTotalAmount())
                .shippingFee(requestDto.nonClientOrderPriceInfoDto().shippingFee())
                .designatedDeliveryDate(requestDto.designatedDeliveryDate())
                .phoneNumber(requestDto.nonClientOrdererInfoDto().phoneNumber())
                .deliveryAddress(address.toString())
                .nonClientOrderPassword(requestDto.nonClientOrdererInfoDto().nonClientOrderPassword())
                .nonClientOrdererName(requestDto.nonClientOrdererInfoDto().nonClientOrdererName())
                .nonClientOrdererEmail(requestDto.nonClientOrdererInfoDto().nonClientOrdererEmail())
                .build();

        // OrderProductDetail 생성 + OrderProductDetailOption 생성
        requestDto.orderedProductAndOptionProductPairDtoList().forEach((productAndOptionProductPair) -> {
            commonOrderService.createOrderProductDetailAndOption(order, productAndOptionProductPair);
        });

    }

    @Override
    public FindNonClientOrderDetailResponseDto findNonClientOrderDetail(long orderId, String orderPassword) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        return null;
    }

    @Override
    public long findNonClientOrderId(FindNonClientOrderIdRequestDto requestDto) {
        // TODO unique 제약조건 추가
        Order order = orderRepository.findNonClientOrderId(requestDto.ordererName(),
                requestDto.phoneNumber(),
                requestDto.email()).
                orElseThrow(OrderNotFoundException::new);
        return order.getOrderId();
    }

    @Override
    public String findNonClientOrderPassword(FindNonClientOrderPasswordRequestDto requestDto) {
        Order order = orderRepository.findNonClientOrderPassword(requestDto.orderId(),
                requestDto.ordererName(),
                requestDto.ordererName(),
                requestDto.email()).orElseThrow(OrderNotFoundException::new);
        return order.getNonClientOrderPassword();
    }

}
