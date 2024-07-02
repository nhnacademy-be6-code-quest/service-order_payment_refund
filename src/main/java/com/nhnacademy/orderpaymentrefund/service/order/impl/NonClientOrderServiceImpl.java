package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.converter.impl.NonClientOrderConverter;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
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
    private ProductOrderDetailRepository productOrderDetailRepository;
    private ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    // convreter
    private NonClientOrderConverter nonClientOrderConverter;
    private ProductOrderDetailConverter productOrderDetailConverter;
    private ProductOrderDetailOptionConverter productOrderDetailOptionConverter;

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
        // TODO 비회원일때 쿠키 초기화.
    }

    @Override
    public void createOrder(CreateNonClientOrderRequestDto requestDto) {

        // Order 생성
        Order order = nonClientOrderConverter.dtoToEntity(requestDto);
        orderRepository.save(order);

        // OrderProductDetail + OrderProductDetailOption 생성 및 저장
        requestDto.orderedProductAndOptionProductPairDtoList().forEach((pair) -> {
            ProductOrderDetail productOrderDetail = productOrderDetailConverter.dtoToEntity(pair.productOrderDetailDto(), order);
            productOrderDetailRepository.save(productOrderDetail);
            pair.productOrderDetailOptionDtoList().forEach((optionDto) -> {
                ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionConverter.dtoToEntity(optionDto, productOrderDetail);
                productOrderDetailOptionRepository.save(productOrderDetailOption);
            });
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
