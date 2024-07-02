package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.converter.impl.NonClientOrderConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.field.NonClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailOptionDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateNonClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class NonClientOrderServiceImpl implements NonClientOrderService {

    private OrderRepository orderRepository;
    private ProductOrderDetailRepository productOrderDetailRepository;
    private ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    // converter
    private NonClientOrderConverterImpl nonClientOrderConverter;
    private ProductOrderDetailConverterImpl productOrderDetailConverter;
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
        // TODO 구현
    }

    @Override
    public void postprocessing() {
        // TODO 구현
        // TODO 비회원일때 쿠키 초기화.
    }

    @Override
    public void createOrder(CreateNonClientOrderRequestDto requestDto) {

        // 비회원 Order 생성
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
    public FindNonClientOrderResponseDto findNonClientOrder(long orderId, String orderPassword) {

        Order order = orderRepository.findByOrderIdAndNonClientOrderPassword(orderId, orderPassword).orElseThrow(OrderNotFoundException::new);

        List<OrderedProductAndOptionProductPairDto> orderedProductAndOptionProductPairDtoList = order.getProductOrderDetailList().stream().map((productOrderDetail) -> {

            ProductOrderDetailDto productOrderDetailDto = productOrderDetailConverter.entityToDto(productOrderDetail);

            List<ProductOrderDetailOptionDto> productOrderDetailOptionDtoList = productOrderDetail.getProductOrderDetailOptionList().stream().map((option) -> {
                return productOrderDetailOptionConverter.entityToDto(option);
            }).toList();


            return OrderedProductAndOptionProductPairDto.builder()
                    .productOrderDetailDto(productOrderDetailDto)
                    .productOrderDetailOptionDtoList(productOrderDetailOptionDtoList)
                    .build();

        }).toList();

        NonClientOrderPriceInfoDto nonClientOrderPriceInfoDto = NonClientOrderPriceInfoDto.builder()
                .shippingFee(order.getShippingFee())
                .productTotalAmount(order.getProductTotalAmount())
                .payAmount(1000000) // TODO payment에서 지불금액 가져오기
                .build();

        // TODO 컨버터 인자로 Payment 추가로 넘기기
        return nonClientOrderConverter.entityToDto(order, "결제수단", nonClientOrderPriceInfoDto, orderedProductAndOptionProductPairDtoList);

    }

    @Override
    public Page<FindNonClientOrderIdInfoResponseDto> findNonClientOrderId(FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, Pageable pageable) {

        return orderRepository.findAllByNonClientOrdererEmailAndPhoneNumberAndNonClientOrdererName(findNonClientOrderIdRequestDto.email(),
                findNonClientOrderIdRequestDto.phoneNumber(),
                findNonClientOrderIdRequestDto.ordererName(), pageable).map((order) ->
            FindNonClientOrderIdInfoResponseDto.builder()
                    .orderDateTime(order.getOrderDatetime())
                    .orderId(order.getOrderId())
                    .build());

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
