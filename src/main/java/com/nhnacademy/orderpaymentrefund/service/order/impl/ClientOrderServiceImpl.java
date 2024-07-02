package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import com.nhnacademy.orderpaymentrefund.service.order.CommonOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {

    private OrderRepository orderRepository;
    private CommonOrderService commonOrderService;

    @Override
    public URI tryCreateOrder(HttpHeaders headers, CreateClientOrderRequestDto createClientOrderRequestDto) {
        long clientId = 1L;
        preprocessing();
        createOrder(clientId, createClientOrderRequestDto);
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
    public void createOrder(long clientId, CreateClientOrderRequestDto requestDto) {

        // 배송 주소
        StringBuilder address = new StringBuilder();
        address.append(requestDto.clientOrdererInfoDto().zipCode());
        address.append(", ");
        address.append(requestDto.clientOrdererInfoDto().address());
        address.append(", ");
        address.append(requestDto.clientOrdererInfoDto().detailAddress());

        // 회원 Order 생성
        Order order = Order.clientOrderBuilder()
                .clientId(clientId)
                .couponId(requestDto.couponId())
                .pointPolicyId(requestDto.pointPolicyId())
                .tossOrderId(UUID.randomUUID().toString())
                .productTotalAmount(requestDto.clientOrderPriceInfoDto().productTotalAmount())
                .shippingFee(requestDto.clientOrderPriceInfoDto().shippingFee())
                .designatedDeliveryDate(requestDto.designatedDeliveryDate())
                .phoneNumber(requestDto.clientOrdererInfoDto().phoneNumber())
                .deliveryAddress(requestDto.clientOrdererInfoDto().address())
                .discountAmountByCoupon(Optional.ofNullable(requestDto.clientOrderPriceInfoDto().couponDiscountAmount()).orElse(0L))
                .discountAmountByPoint(Optional.ofNullable(requestDto.clientOrderPriceInfoDto().usedPointDiscountAmount()).orElse(0L))
                .accumulatedPoint(requestDto.accumulatedPoint())
                .build();

        // OrderProductDetail 생성 + OrderProductDetailOption 생성
        requestDto.orderedProductAndOptionProductPairDtoList().forEach((productAndOptionProductPair) -> {
            commonOrderService.createOrderProductDetailAndOption(order, productAndOptionProductPair);
        });

    }

    @Override
    public Page<FindClientOrderResponseDto> findClientOrderList(HttpHeaders headers, Pageable pageable) {
     return null;
    }

    @Override
    public FindClientOrderDetailResponseDto findClientOrderDetail(long orderId) {
        return null;
    }

}
