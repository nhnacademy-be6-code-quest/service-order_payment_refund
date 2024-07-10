package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.client.TestOtherService;
import com.nhnacademy.orderpaymentrefund.converter.impl.ClientOrderConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ClientOrderPriceInfoDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.OrderedProductAndOptionProductPairDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailDto;
import com.nhnacademy.orderpaymentrefund.dto.order.field.ProductOrderDetailOptionDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderListGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindClientOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelOrder;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.WrongClientAccessToOrder;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {

    private static final String ID_HEADER = "X-User-Id";

    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final OrderRepository orderRepository;

    // converter
    private final ClientOrderConverterImpl clientOrderConverter;
    private final ProductOrderDetailConverterImpl productOrderDetailConverter;
    private final ProductOrderDetailOptionConverter productOrderDetailOptionConverter;

    // TODO 임의
    private final TestOtherService testOtherService;

    @Override
    public Long tryCreateOrder(HttpHeaders headers, ClientOrderFormRequestDto clientOrderForm) {
        long clientId = getClientId(headers);
        preprocessing();
        Long orderId = createOrder(clientId, clientOrderForm);
        //tryPay();
        postprocessing();
        return orderId;
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
        testOtherService.checkStock(true);
        testOtherService.checkCouponAvailability(true);
        testOtherService.checkPointAvailability(true);
        testOtherService.checkAccumulatePointAvailability(true);
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
        testOtherService.processDroppingStock(true);
        testOtherService.processUsedCoupon(true);
        testOtherService.processUsedPoint(true);
        testOtherService.processAccumulatePoint(true);
    }

    @Override
    public Long createOrder(long clientId, ClientOrderFormRequestDto requestDto) {

        // 회원 Order 생성 및 저장
        Order order = clientOrderConverter.dtoToEntity(requestDto, clientId);
        orderRepository.save(order);

        // OrderProductDetail + OrderProductDetailOption 생성 및 저장
        requestDto.getOrderDetailDtoItemList().forEach((item) -> {
            ProductOrderDetail productOrderDetail = productOrderDetailConverter.dtoToEntity(item, order);
            productOrderDetailRepository.save(productOrderDetail);
            if(item.getUsePackaging()){
                ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionConverter.dtoToEntity(item, productOrderDetail);
                productOrderDetailOptionRepository.save(productOrderDetailOption);
            }
        });

        return order.getOrderId();

    }

    @Override
    public Page<FindClientOrderResponseDto> findClientOrderList(HttpHeaders headers, Pageable pageable) {

        long clientId = getClientId(headers);

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

    @Override
    public Page<OrderResponseDto> getOrders(HttpHeaders headers, int pageSize, int pageNo, String sortBy, String sortDir) {

        long clientId = getClientId(headers);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return orderRepository.findByClientId(clientId, PageRequest.of(pageNo, pageSize, sort)).map(order -> {

            if(!order.getClientId().equals(clientId)) throw new WrongClientAccessToOrder();

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
                    .designatedDeliveryDate(order.getDesignatedDeliveryDate() == null ? null : order.getDesignatedDeliveryDate().toString())
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
        });
    }

    @Override
    public OrderResponseDto getOrder(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) throw new WrongClientAccessToOrder();

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
    public void cancelOrder(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        if(!(order.getOrderStatus() == OrderStatus.WAIT_PAYMENT || order.getOrderStatus() == OrderStatus.PAYED)){
            throw new CannotCancelOrder("결제대기 또는 결제완료 상태에서 주문취소 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.CANCEL);
        orderRepository.save(order);

    }

    @Override
    public void refundOrder(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        if(!(order.getOrderStatus() == OrderStatus.DELIVERING || order.getOrderStatus() == OrderStatus.DELIVERY_COMPLETE)){
            throw new CannotCancelOrder("배송중 또는 배송완료 상태에서 주문취소 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.REFUND);
        orderRepository.save(order);

    }

    @Override
    public void paymentCompleteOrder(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        if(order.getOrderStatus() != OrderStatus.WAIT_PAYMENT){
            throw new CannotCancelOrder("결제대기 상태일때만 결제완료 상태로 변경할 수 있습니다.");
        }

        order.updateOrderStatus(OrderStatus.DELIVERY_COMPLETE);
        orderRepository.save(order);

    }

    @Override
    public String getOrderStatus(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        return order.getOrderStatus().kor;

    }

    private long getClientId(HttpHeaders headers){
        if (headers.get(ID_HEADER) == null){
            throw new RuntimeException("clientId is null");
        }
        return Long.parseLong(headers.getFirst(ID_HEADER));
    }
}
