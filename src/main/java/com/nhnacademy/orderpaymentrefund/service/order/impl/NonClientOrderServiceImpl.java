package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.client.TestOtherService;
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
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class NonClientOrderServiceImpl implements NonClientOrderService {

    private final OrderRepository orderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    // converter
    private final NonClientOrderConverterImpl nonClientOrderConverter;
    private final ProductOrderDetailConverterImpl productOrderDetailConverter;
    private final ProductOrderDetailOptionConverter productOrderDetailOptionConverter;
    // testOtherService
    private final TestOtherService testOtherService;

    @Transactional
    @Override
    public void tryCreateOrder(NonClientOrderFormRequestDto requestDto) {
        preprocessing();
        createOrder(requestDto);
        postprocessing();
    }

    @Override
    public void preprocessing() {
        // TODO 구현
        testOtherService.checkStock(true);
    }

    @Override
    public void postprocessing() {
        // TODO 구현
        // TODO 비회원일때 쿠키 초기화.
        testOtherService.processDroppingStock(true);
    }

    @Override
    public void createOrder(NonClientOrderFormRequestDto requestDto) {

        // 비회원 Order 생성
        Order order = nonClientOrderConverter.dtoToEntity(requestDto);
        orderRepository.save(order);

        // OrderProductDetail + OrderProductDetailOption 생성 및 저장
        requestDto.getOrderDetailDtoItemList().forEach((item) -> {
            ProductOrderDetail productOrderDetail = productOrderDetailConverter.dtoToEntity(item, order);
            productOrderDetailRepository.save(productOrderDetail);
            if(Objects.nonNull(item.getPackableProduct()) || item.getUsePackaging().booleanValue() == true){
                ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionConverter.dtoToEntity(item, productOrderDetail);
                productOrderDetailOptionRepository.save(productOrderDetailOption);
            }
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
        return orderRepository.findNonClientOrderIdList(findNonClientOrderIdRequestDto, pageable).map((order) ->
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
