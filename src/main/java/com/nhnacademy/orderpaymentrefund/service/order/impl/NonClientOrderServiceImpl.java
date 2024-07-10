package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.client.TestOtherService;
import com.nhnacademy.orderpaymentrefund.converter.impl.NonClientOrderConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
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
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.*;
import com.nhnacademy.orderpaymentrefund.exception.type.ForbiddenExceptionType;
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
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URI;
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

    @Transactional
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
    public OrderResponseDto getOrder(HttpHeaders headers, Long orderId, String orderPassword) {
        checkNonClient(headers);
        
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getNonClientOrderPassword().equals(orderPassword)){
            throw new OrderPasswordNotCorrect();
        }

        OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .clientId(order.getClientId())
                .couponId(order.getCouponId())
                .tossOrderId(order.getTossOrderId())
                .orderDatetime(order.getOrderDatetime().toString())
                .orderStatus(order.getOrderStatus().kor)
                .productTotalAmount(order.getOrderTotalAmount())
                .shippingFee(order.getShippingFee())
                .orderTotalAmount(order.getOrderTotalAmount())
                .designatedDeliveryDate(order.getDesignatedDeliveryDate().toString())
                .phoneNumber(order.getPhoneNumber())
                .deliveryAddress(order.getDeliveryAddress())
                .discountAmountByCoupon(order.getDiscountAmountByCoupon() == null ? 0 : order.getDiscountAmountByCoupon())
                .discountAmountByPoint(order.getDiscountAmountByPoint() == null ? 0 : order.getDiscountAmountByPoint())
                .accumulatedPoint(order.getAccumulatedPoint() == null ? 0 : order.getAccumulatedPoint())
                .deliveryStartDate(order.getDeliveryStartDate() == null ? null : order.getDeliveryStartDate().toString())
                .nonClientOrderPassword(order.getNonClientOrderPassword())
                .nonClientOrdererName(order.getNonClientOrdererName())
                .nonClientOrdererEmail(order.getNonClientOrdererEmail())
                .build();

        productOrderDetailRepository.findAllByOrder(order).forEach(productOrderDetail -> {
            ProductOrderDetailOption option = productOrderDetailOptionRepository.findFirstByProductOrderDetail(productOrderDetail);
            orderResponseDto.addClientOrderListItem(
                    OrderResponseDto.ClientOrderListItem.builder()
                            .productId(productOrderDetail.getProductId())
                            .productName(productOrderDetail.getProductName())
                            .productQuantity(productOrderDetail.getQuantity())
                            .productSinglePrice(option != null ? option.getOptionProductPrice() : 0)
                            .optionProductId(option != null ? option.getProductId() : null)
                            .optionProductName(option != null ? option.getOptionProductName() : null)
                            .optionProductQuantity(option != null ? option.getQuantity() : 0)
                            .build()
            );
        });

        return orderResponseDto;
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
