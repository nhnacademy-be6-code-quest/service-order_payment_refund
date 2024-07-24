package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelOrder;
import com.nhnacademy.orderpaymentrefund.exception.ClientCannotAccessNonClientService;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.NonClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NonClientOrderServiceImpl implements NonClientOrderService {

    private static final String ID_HEADER = "X-User-Id";
    private static final String ID_KEY = "order";
    private final OrderRepository orderRepository;
    private final NonClientOrderRepository nonClientOrderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveNonClientTemporalOrder(HttpHeaders headers, NonClientOrderForm requestDto) {
        checkNonClient(headers);
        String orderCode = requestDto.getOrderCode();
        redisTemplate.opsForHash().put(ID_KEY, orderCode, requestDto);
        Object data = redisTemplate.opsForHash().get(ID_KEY, orderCode);
        log.info("data: {}", data);
    }

    @Override
    public NonClientOrderForm getNonClientTemporalOrder(HttpHeaders headers, String orderCode) {
        checkNonClient(headers);
        return (NonClientOrderForm) redisTemplate.opsForHash().get(ID_KEY, orderCode);
    }

    @Override
    public Page<FindNonClientOrderIdInfoResponseDto> findNonClientOrderId(HttpHeaders headers,
        FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, Pageable pageable) {

        checkNonClient(headers);

        nonClientOrderRepository.findByNonClientOrdererNameAndNonClientOrdererEmailAndOrder_PhoneNumber(
            findNonClientOrderIdRequestDto.ordererName(), findNonClientOrderIdRequestDto.email(),
            findNonClientOrderIdRequestDto.phoneNumber(), pageable);

        return nonClientOrderRepository.findByNonClientOrdererNameAndNonClientOrdererEmailAndOrder_PhoneNumber(
                findNonClientOrderIdRequestDto.ordererName(), findNonClientOrderIdRequestDto.email(),
                findNonClientOrderIdRequestDto.phoneNumber(), pageable)
            .map(nonClientOrder ->
                FindNonClientOrderIdInfoResponseDto.builder()
                    .orderDateTime(nonClientOrder.getOrder().getOrderDatetime())
                    .orderId(nonClientOrder.getOrder().getOrderId())
                    .build());
    }

    @Override
    public String findNonClientOrderPassword(HttpHeaders headers,
        FindNonClientOrderPasswordRequestDto requestDto) {

        checkNonClient(headers);

        NonClientOrder nonClientOrder = nonClientOrderRepository.findByNonClientOrdererNameAndNonClientOrdererEmailAndOrder_PhoneNumber(
            requestDto.getOrdererName(), requestDto.getEmail(), requestDto.getPhoneNumber()).orElseThrow(OrderNotFoundException::new);

        return nonClientOrder.getNonClientOrderPassword();
    }

    @Override
    public NonClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId,
        String orderPassword) {
        checkNonClient(headers);

        NonClientOrder nonClientOrder = nonClientOrderRepository.findByNonClientOrderPasswordEqualsAndOrder_OrderId(orderPassword, orderId).orElseThrow(OrderNotFoundException::new);
        Order order = nonClientOrder.getOrder();

        NonClientOrderGetResponseDto nonClientOrderGetResponseDto = NonClientOrderGetResponseDto.builder()
            .orderId(order.getOrderId())
            .orderCode(order.getOrderCode())
            .orderDatetime(order.getOrderDatetime().toString().split("T")[0])
            .orderStatus(order.getOrderStatus().kor)
            .productTotalAmount(order.getProductTotalAmount())
            .shippingFee(order.getShippingFee())
            .orderTotalAmount(order.getOrderTotalAmount())
            .designatedDeliveryDate(order.getDesignatedDeliveryDate() == null ? null
                : order.getDesignatedDeliveryDate().toString())
            .deliveryStartDate(order.getDeliveryStartDate() == null ? null
                : order.getDeliveryStartDate().toString())
            .phoneNumber(order.getPhoneNumber())
            .deliveryAddress(order.getDeliveryAddress())
            .nonClientOrderPassword(nonClientOrder.getNonClientOrderPassword())
            .nonClientOrdererName(nonClientOrder.getNonClientOrdererName())
            .nonClientOrdererEmail(nonClientOrder.getNonClientOrdererEmail())
            .build();

        List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder_OrderId(orderId);

        for (ProductOrderDetail productOrderDetail : orderDetailList) {

            ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
                productOrderDetail.getProductOrderDetailId()).orElse(null);

            NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem nonClientProductOrderDetailListItem =
                NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem.builder()
                    .productId(productOrderDetail.getProductOrderDetailId())
                    .productName(productOrderDetail.getProductName())
                    .productQuantity(productOrderDetail.getQuantity())
                    .productSinglePrice(productOrderDetail.getPricePerProduct())
                    .optionProductId(productOrderDetailOption == null ? null
                        : productOrderDetailOption.getProductId())
                    .optionProductName(productOrderDetailOption == null ? null
                        : productOrderDetailOption.getOptionProductName())
                    .optionProductQuantity(productOrderDetailOption == null ? null
                        : productOrderDetailOption.getQuantity())
                    .optionProductSinglePrice(productOrderDetailOption == null ? null
                        : productOrderDetailOption.getOptionProductPrice())
                    .build();

            nonClientOrderGetResponseDto.addNonClientProductOrderDetailList(
                nonClientProductOrderDetailListItem);

        }

        return nonClientOrderGetResponseDto;
    }

    @Override
    public void cancelOrder(HttpHeaders headers, long orderId) {

        checkNonClient(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (!(order.getOrderStatus() == OrderStatus.WAIT_PAYMENT
            || order.getOrderStatus() == OrderStatus.PAYED)) {
            throw new CannotCancelOrder("결제대기 또는 결제완료 상태에서 주문취소 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.CANCEL);
        orderRepository.save(order);
    }

    @Override
    public void refundOrder(HttpHeaders headers, long orderId) {

        checkNonClient(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (!(order.getOrderStatus() == OrderStatus.DELIVERING
            || order.getOrderStatus() == OrderStatus.DELIVERY_COMPLETE)) {
            throw new CannotCancelOrder("배송중 또는 배송완료 상태에서 주문취소 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.REFUND);
        orderRepository.save(order);
    }

    private void checkNonClient(HttpHeaders headers) {
        if (headers.get(ID_HEADER) != null) {
            throw new ClientCannotAccessNonClientService();
        }
    }
}
