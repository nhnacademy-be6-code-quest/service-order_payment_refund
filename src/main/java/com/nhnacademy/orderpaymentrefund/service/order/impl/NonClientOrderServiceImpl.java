package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderPasswordRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderIdInfoResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.ClientCannotAccessNonClientService;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.NonClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.NonClientOrderService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NonClientOrderServiceImpl implements NonClientOrderService {

    private static final String ID_KEY = "order";
    private final NonClientOrderRepository nonClientOrderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;

    private final ClientHeaderContext clientHeaderContext;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveNonClientTemporalOrder(HttpHeaders headers, NonClientOrderForm requestDto) {
        checkNonClient();
        String orderCode = requestDto.getOrderCode();
        redisTemplate.opsForHash().put(ID_KEY, orderCode, requestDto);
    }

    @Override
    public NonClientOrderForm getNonClientTemporalOrder(HttpHeaders headers, String orderCode) {
        checkNonClient();
        return (NonClientOrderForm) redisTemplate.opsForHash().get(ID_KEY, orderCode);
    }

    @Override
    public Page<FindNonClientOrderIdInfoResponseDto> findNonClientOrderId(HttpHeaders headers,
        FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, Pageable pageable) {

        checkNonClient();

        Page<NonClientOrder> nonClientOrderPage =  nonClientOrderRepository.findByNonClientOrdererNameAndNonClientOrdererEmailAndOrder_PhoneNumber(
            findNonClientOrderIdRequestDto.ordererName(), findNonClientOrderIdRequestDto.email(),
            findNonClientOrderIdRequestDto.phoneNumber(), pageable);

        List<FindNonClientOrderIdInfoResponseDto> responseDtoList = new ArrayList<>();

        for(NonClientOrder nonClientOrder : nonClientOrderPage.getContent()) {
            FindNonClientOrderIdInfoResponseDto responseDto = FindNonClientOrderIdInfoResponseDto.builder()
                .orderDateTime(nonClientOrder.getOrder().getOrderDatetime())
                .orderId(nonClientOrder.getOrder().getOrderId())
                .build();
            responseDtoList.add(responseDto);
        }

        return new PageImpl<>(responseDtoList, pageable, nonClientOrderPage.getTotalElements());
    }

    @Override
    public String findNonClientOrderPassword(HttpHeaders headers,
        FindNonClientOrderPasswordRequestDto requestDto) {

        checkNonClient();

        NonClientOrder nonClientOrder = nonClientOrderRepository.findByNonClientOrdererNameAndNonClientOrdererEmailAndOrder_PhoneNumber(
            requestDto.getOrdererName(), requestDto.getEmail(), requestDto.getPhoneNumber()).orElseThrow(OrderNotFoundException::new);

        return nonClientOrder.getNonClientOrderPassword();
    }

    @Override
    public NonClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId,
        String orderPassword) {

        checkNonClient();

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

            Optional<ProductOrderDetailOption> productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
                productOrderDetail.getProductOrderDetailId());

            NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem nonClientProductOrderDetailListItem =
                NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem.builder()
                    .productId(productOrderDetail.getProductOrderDetailId())
                    .productName(productOrderDetail.getProductName())
                    .productQuantity(productOrderDetail.getQuantity())
                    .productSinglePrice(productOrderDetail.getPricePerProduct())
                    .optionProductId(productOrderDetailOption.isEmpty() ? null
                        : productOrderDetailOption.get().getProductId())
                    .optionProductName(productOrderDetailOption.isEmpty() ? null
                        : productOrderDetailOption.get().getOptionProductName())
                    .optionProductQuantity(productOrderDetailOption.isEmpty() ? null
                        : productOrderDetailOption.get().getQuantity())
                    .optionProductSinglePrice(productOrderDetailOption.isEmpty() ? null
                        : productOrderDetailOption.get().getOptionProductPrice())
                    .build();

            nonClientOrderGetResponseDto.addNonClientProductOrderDetailList(
                nonClientProductOrderDetailListItem);

        }

        return nonClientOrderGetResponseDto;
    }

    private void checkNonClient() {
        if (clientHeaderContext.isClient()) {
            throw new ClientCannotAccessNonClientService();
        }
    }
}
