package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.converter.impl.ClientOrderConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailOptionDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.CreateClientOrderRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {

    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final OrderRepository orderRepository;

    // converter
    private final ClientOrderConverterImpl clientOrderConverter;
    private final ProductOrderDetailConverterImpl productOrderDetailConverter;
    private final ProductOrderDetailOptionConverter productOrderDetailOptionConverter;

    @Override
    public URI tryCreateOrder(HttpHeaders headers, CreateClientOrderRequestDto createClientOrderRequestDto) {
        long clientId = 1L;
        preprocessing();
        createOrder(clientId, createClientOrderRequestDto);
        //tryPay();
        postprocessing();
        return null;
    }

    @Override
    public void preprocessing() {
        // TODO 구현
        /*
        * 1. 재고확인(메인 상품 + 옵션 상품) 물량 확보하기.
        * 2. 포인트 사용 가능 여부 체크
        * 3. 쿠폰 유효성 체크
        * 4. 적립금 유효성 체크
        * */
    }

    @Override
    public void postprocessing() {
        // TODO 구현
        /*
         * 1. 재고 감소
         * 2. 포인트 감소
         * 3. 주문, 결제데이터 저장 (?)
         * 4. 적립금 부여
         * 5. 쿠폰 사용 처리
         * */
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

        long clientId = 1L;

        return orderRepository.findByClientId(clientId, pageable).map((order) -> {

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

            ClientOrderPriceInfoDto clientOrderPriceInfoDto = ClientOrderPriceInfoDto.builder()
                    .shippingFee(order.getShippingFee())
                    .productTotalAmount(order.getProductTotalAmount())
                    .payAmount(100000) // TOO payment에서 지불금액 가져오기
                    .couponDiscountAmount(order.getDiscountAmountByCoupon())
                    .usedPointDiscountAmount(order.getDiscountAmountByPoint())
                    .build();

            return clientOrderConverter.entityToDto(order, "결제수단", clientOrderPriceInfoDto, orderedProductAndOptionProductPairDtoList);
        });

    }

}
