package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;

    @Override
    public PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(HttpHeaders headers, long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        Long discountAmountByCoupon = order.getDiscountAmountByCoupon();
        Long discountAmountByPoint = order.getDiscountAmountByPoint();
        Long sizeProductOrderDetail = productOrderDetailRepository.getSizeByOrderId(orderId);

        StringBuilder orderHistoryTitle = new StringBuilder();
        orderHistoryTitle.append(productOrderDetailRepository.findFirstByOrder(order).orElseThrow(OrderNotFoundException::new).getProductName());
        orderHistoryTitle.append(String.format(" 외 %d건", sizeProductOrderDetail));


        return PaymentOrderShowRequestDto.builder()
                .orderTotalAmount(order.getOrderTotalAmount())
                .discountAmountByCoupon(discountAmountByCoupon == null ? 0 : discountAmountByCoupon)
                .discountAmountByPoint(discountAmountByPoint == null ? 0 : discountAmountByPoint)
                .tossOrderId(order.getTossOrderId())
                .orderHistoryTitle(orderHistoryTitle.toString())
                .build();
    }

    @Override
    public PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(HttpHeaders headers, long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        Long discountAmountByCoupon = order.getDiscountAmountByCoupon();
        Long discountAmountByPoint = order.getDiscountAmountByPoint();

        List<PaymentOrderApproveRequestDto.ProductOrderDetailOptionRequestDto> productOrderDetailOptionRequestDtoList = new ArrayList<>();
        List<PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto> productOrderDetailList = new ArrayList<>();

        productOrderDetailRepository.findAllByOrder(order).forEach(productOrderDetail -> {

            productOrderDetailOptionRepository.findByProductOrderDetail(productOrderDetail).forEach(productOrderDetailOption -> {
                productOrderDetailOptionRequestDtoList.add(PaymentOrderApproveRequestDto.ProductOrderDetailOptionRequestDto.builder()
                                .productId(productOrderDetailOption.getProductId())
                                .optionProductQuantity(productOrderDetailOption.getQuantity())
                                .build());
            });

            productOrderDetailList.add(PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto.builder()
                    .productId(productOrderDetail.getProductId())
                    .quantity(productOrderDetail.getQuantity())
                    .productOrderDetailOptionRequestDtoList(productOrderDetailOptionRequestDtoList)
                    .build());

        });

        return PaymentOrderApproveRequestDto.builder()
                .orderTotalAmount(order.getOrderTotalAmount())
                .discountAmountByCoupon(discountAmountByCoupon == null ? 0 : discountAmountByCoupon)
                .discountAmountByPoint(discountAmountByPoint == null ? 0 : discountAmountByPoint)
                .tossOrderId(order.getTossOrderId())
                .clientId(order.getClientId())
                .couponId(order.getCouponId())
                .accumulatedPoint(0)
                .productOrderDetailList(productOrderDetailList)
                .build();

    }


}
