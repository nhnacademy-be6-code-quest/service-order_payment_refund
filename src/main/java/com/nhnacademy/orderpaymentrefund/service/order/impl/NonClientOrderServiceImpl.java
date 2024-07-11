package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.client.TestOtherService;
import com.nhnacademy.orderpaymentrefund.converter.impl.NonClientOrderConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelOrder;
import com.nhnacademy.orderpaymentrefund.exception.ClientCannotAccessNonClientService;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class NonClientOrderServiceImpl implements NonClientOrderService {

    private static final String ID_HEADER = "X-User-Id";

    private final OrderRepository orderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    // converter
    private final NonClientOrderConverterImpl nonClientOrderConverter;
    private final ProductOrderDetailConverterImpl productOrderDetailConverter;
    private final ProductOrderDetailOptionConverter productOrderDetailOptionConverter;
    // testOtherService
    private final TestOtherService testOtherService;

    @Override
    public Long tryCreateOrder(HttpHeaders headers, NonClientOrderFormRequestDto requestDto) {
        checkNonClient(headers);
        Long orderId = createOrder(requestDto);
        return orderId;
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
    public Long createOrder(NonClientOrderFormRequestDto requestDto) {

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

        return order.getOrderId();
    }

    @Override
    public Page<FindNonClientOrderIdInfoResponseDto> findNonClientOrderId(HttpHeaders headers, FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, Pageable pageable) {
        checkNonClient(headers);
        return orderRepository.findNonClientOrderIdList(findNonClientOrderIdRequestDto, pageable).map((order) ->
            FindNonClientOrderIdInfoResponseDto.builder()
                    .orderDateTime(order.getOrderDatetime())
                    .orderId(order.getOrderId())
                    .build());
    }

    @Override
    public String findNonClientOrderPassword(HttpHeaders headers, FindNonClientOrderPasswordRequestDto requestDto) {
        checkNonClient(headers);
        Order order = orderRepository.findNonClientOrderPassword(requestDto.orderId(),
                requestDto.ordererName(),
                requestDto.ordererName(),
                requestDto.email()).orElseThrow(OrderNotFoundException::new);
        return order.getNonClientOrderPassword();
    }

    @Override
    public NonClientOrderGetResponseDto getOrder(HttpHeaders headers, Long orderId, String orderPassword) {
        checkNonClient(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        NonClientOrderGetResponseDto nonClientOrderGetResponseDto = NonClientOrderGetResponseDto.builder()
                .orderId(order.getOrderId())
                .tossOrderId(order.getTossOrderId())
                .orderDatetime(order.getOrderDatetime().toString())
                .orderStatus(order.getOrderStatus().kor)
                .productTotalAmount(order.getProductTotalAmount())
                .shippingFee(order.getShippingFee())
                .orderTotalAmount(order.getOrderTotalAmount())
                .designatedDeliveryDate(order.getDesignatedDeliveryDate() == null ? null : order.getDesignatedDeliveryDate().toString())
                .deliveryStartDate(order.getDeliveryStartDate() == null ? null : order.getDeliveryStartDate().toString())
                .phoneNumber(order.getPhoneNumber())
                .deliveryAddress(order.getDeliveryAddress())
                .nonClientOrderPassword(order.getNonClientOrderPassword())
                .nonClientOrdererName(order.getNonClientOrdererName())
                .nonClientOrdererEmail(order.getNonClientOrdererEmail())
                .build();

        List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder(order);

        for(ProductOrderDetail productOrderDetail : orderDetailList){

            ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail(productOrderDetail);

            NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem nonClientProductOrderDetailListItem =
                    NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem.builder()
                            .productId(productOrderDetail.getProductOrderDetailId())
                            .productName(productOrderDetail.getProductName())
                            .productQuantity(productOrderDetail.getQuantity())
                            .productSinglePrice(productOrderDetail.getPricePerProduct())
                            .optionProductId(productOrderDetailOption == null ? null : productOrderDetailOption.getProductId())
                            .optionProductName(productOrderDetailOption == null ? null : productOrderDetailOption.getOptionProductName())
                            .optionProductQuantity(productOrderDetailOption == null ? null : productOrderDetailOption.getQuantity())
                            .optionProductPrice(productOrderDetailOption == null ? null : productOrderDetailOption.getOptionProductPrice())
                            .build();

            nonClientOrderGetResponseDto.addNonClientProductOrderDetailList(nonClientProductOrderDetailListItem);

        }


        return nonClientOrderGetResponseDto;
    }

    @Override
    public void paymentCompleteOrder(HttpHeaders headers, long orderId) {

        checkNonClient(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(order.getOrderStatus() != OrderStatus.WAIT_PAYMENT){
            throw new CannotCancelOrder("결제대기 상태일때만 결제완료 상태로 변경할 수 있습니다.");
        }

        order.updateOrderStatus(OrderStatus.DELIVERY_COMPLETE);
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(HttpHeaders headers, long orderId) {

        checkNonClient(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!(order.getOrderStatus() == OrderStatus.WAIT_PAYMENT || order.getOrderStatus() == OrderStatus.PAYED)){
            throw new CannotCancelOrder("결제대기 또는 결제완료 상태에서 주문취소 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.CANCEL);
        orderRepository.save(order);
    }

    @Override
    public void refundOrder(HttpHeaders headers, long orderId) {

        checkNonClient(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!(order.getOrderStatus() == OrderStatus.DELIVERING || order.getOrderStatus() == OrderStatus.DELIVERY_COMPLETE)){
            throw new CannotCancelOrder("배송중 또는 배송완료 상태에서 주문취소 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.REFUND);
        orderRepository.save(order);
    }

    private void checkNonClient(HttpHeaders headers){
        if(headers.get(ID_HEADER) != null){
            throw new ClientCannotAccessNonClientService();
        }
    }
}
