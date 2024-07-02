package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.converter.impl.ClientOrderConverter;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpHeaders;

@Service
@Slf4j
@AllArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {

    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private OrderRepository orderRepository;

    // converter
    private ClientOrderConverter clientOrderConverter;
    private ProductOrderDetailConverter productOrderDetailConverter;
    private ProductOrderDetailOptionConverter productOrderDetailOptionConverter;

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

        // 회원 Order 생성 및 저장
        Order order = clientOrderConverter.dtoToEntity(requestDto, clientId);
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
    public Page<FindClientOrderResponseDto> findClientOrderList(HttpHeaders headers, Pageable pageable) {

        return null;
    }

    @Override
    public FindClientOrderDetailResponseDto findClientOrderDetail(long orderId) {
        return null;
    }

}
