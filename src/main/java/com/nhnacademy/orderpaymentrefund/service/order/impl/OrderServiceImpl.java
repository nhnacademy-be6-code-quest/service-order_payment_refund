package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.domain.order.ClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderApproveRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.toss.PaymentOrderShowRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.payment.response.paymentView.PaymentViewRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.InvalidOrderChangeAttempt;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.ProductOrderDetailNotFoundException;
import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.NonClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.orderpaymentrefund.service.payment.impl.PaymentStrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final String ID_HEADER = "X-User-Id";
    private static final String REDIS_ORDER_KEY = "order";

    private final OrderRepository orderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final NonClientOrderRepository nonClientOrderRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private final ClientHeaderContext clientHeaderContext;

    private final PaymentStrategyService paymentStrategyService;

    @Override
    public PaymentViewRequestDto getPaymentViewRequestDto(String pgName, String orderCode) {
        return paymentStrategyService.getPaymentViewRequestDto(pgName, orderCode);
    }

    @Override
    public PaymentOrderShowRequestDto getPaymentOrderShowRequestDto(String orderCode) {

        StringBuilder orderHistoryTitle = new StringBuilder();
        Long orderTotalAmount = null;
        Long discountAmountByCoupon = null;
        Long discountAmountByPoint = null;
        Integer sizeProductOrderDetail = null;

        if (clientHeaderContext.isClient()) {

            Object data = redisTemplate.opsForHash().get(REDIS_ORDER_KEY, orderCode);
            ClientOrderCreateForm clientOrderCreateForm = objectMapper.convertValue(data,
                ClientOrderCreateForm.class);

            if (clientOrderCreateForm == null) {
                throw new OrderNotFoundException();
            }

            orderHistoryTitle.append(
                clientOrderCreateForm.getOrderDetailDtoItemList().getFirst().getProductName());

            sizeProductOrderDetail = clientOrderCreateForm.getOrderDetailDtoItemList().size();

            if (clientOrderCreateForm.getOrderDetailDtoItemList().size() > 1) {
                orderHistoryTitle.append(String.format("외 %d개", sizeProductOrderDetail - 1));
            }

            discountAmountByCoupon = clientOrderCreateForm.getCouponDiscountAmount();
            discountAmountByPoint = clientOrderCreateForm.getUsedPointDiscountAmount();
            orderTotalAmount = clientOrderCreateForm.getOrderTotalAmount();

        } else {

            Object data = redisTemplate.opsForHash().get(REDIS_ORDER_KEY, orderCode);
            NonClientOrderForm nonClientOrderForm = objectMapper.convertValue(data,
                NonClientOrderForm.class);

            if (nonClientOrderForm == null) {
                throw new OrderNotFoundException();
            }

            orderHistoryTitle.append(
                nonClientOrderForm.getOrderDetailDtoItemList().getFirst().getProductName());

            sizeProductOrderDetail = nonClientOrderForm.getOrderDetailDtoItemList().size();

            if (nonClientOrderForm.getOrderDetailDtoItemList().size() > 1) {
                orderHistoryTitle.append(String.format("외 %d개", sizeProductOrderDetail - 1));
            }

            orderTotalAmount =
                nonClientOrderForm.getProductTotalAmount() + nonClientOrderForm.getShippingFee();

        }

        return PaymentOrderShowRequestDto.builder()
            .orderTotalAmount(orderTotalAmount == null ? 0 : orderTotalAmount)
            .discountAmountByCoupon(discountAmountByCoupon == null ? 0 : discountAmountByCoupon)
            .discountAmountByPoint(discountAmountByPoint == null ? 0 : discountAmountByPoint)
            .orderCode(orderCode)
            .orderHistoryTitle(orderHistoryTitle.toString())
            .build();
    }

    @Override
    public PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDto(String orderCode) {
        if (clientHeaderContext.isClient()) {
            return getPaymentOrderApproveRequestDtoFromClientOrderForm(orderCode,
                clientHeaderContext.getClientId());
        } else {
            return getPaymentOrderApproveRequestDtoFromNonClientOrderForm(orderCode);
        }
    }

    private PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDtoFromClientOrderForm(
        String orderCode, Long clientId) {

        List<PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto> productOrderDetailList = new ArrayList<>();

        Long discountAmountByCoupon = null;
        Long discountAmountByPoint = null;
        Long orderTotalAmount = null;
        Long couponId = null;
        Long accumulatedPoint = null;

        Object data = redisTemplate.opsForHash().get(REDIS_ORDER_KEY, orderCode);
        ClientOrderCreateForm clientOrderCreateForm = objectMapper.convertValue(data,
            ClientOrderCreateForm.class);

        if (clientOrderCreateForm == null) {
            throw new OrderNotFoundException();
        }

        discountAmountByCoupon = clientOrderCreateForm.getCouponDiscountAmount();
        discountAmountByPoint = clientOrderCreateForm.getUsedPointDiscountAmount();
        orderTotalAmount = clientOrderCreateForm.getOrderTotalAmount();
        couponId = clientOrderCreateForm.getCouponId();

        for (OrderDetailDtoItem orderDetailDtoItem : clientOrderCreateForm.getOrderDetailDtoItemList()) {
            productOrderDetailList.add(
                getProductOrderDetailRequestDto(orderDetailDtoItem.getProductId(),
                    orderDetailDtoItem.getQuantity(), orderDetailDtoItem.getUsePackaging(),
                    orderDetailDtoItem.getOptionProductId(),
                    orderDetailDtoItem.getOptionQuantity()));
        }

        return PaymentOrderApproveRequestDto.builder()
            .orderTotalAmount(orderTotalAmount)
            .discountAmountByPoint(discountAmountByPoint == null ? 0 : discountAmountByPoint)
            .discountAmountByCoupon(discountAmountByCoupon == null ? 0 : discountAmountByCoupon)
            .orderCode(orderCode)
            .clientId(clientId)
            .couponId(couponId)
            .accumulatedPoint(accumulatedPoint == null ? 0 : accumulatedPoint)
            .productOrderDetailList(productOrderDetailList)
            .build();
    }

    private PaymentOrderApproveRequestDto getPaymentOrderApproveRequestDtoFromNonClientOrderForm(
        String orderCode) {

        List<PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto> productOrderDetailList = new ArrayList<>();

        Long orderTotalAmount = null;

        Object data = redisTemplate.opsForHash().get(REDIS_ORDER_KEY, orderCode);
        NonClientOrderForm nonClientOrderForm = objectMapper.convertValue(data,
            NonClientOrderForm.class);

        if (nonClientOrderForm == null) {
            throw new OrderNotFoundException();
        }

        orderTotalAmount =
            nonClientOrderForm.getProductTotalAmount() + nonClientOrderForm.getShippingFee();

        for (OrderDetailDtoItem orderDetailDtoItem : nonClientOrderForm.getOrderDetailDtoItemList()) {
            PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto productOrderDetailRequest = PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto.builder()
                .productId(orderDetailDtoItem.getProductId())
                .quantity(orderDetailDtoItem.getQuantity())
                .productOrderDetailOptionRequestDtoList(
                    Boolean.TRUE.equals(orderDetailDtoItem.getUsePackaging()) ? new ArrayList<>(
                        List.of(
                            PaymentOrderApproveRequestDto.ProductOrderDetailOptionRequestDto.builder()
                                .productId(orderDetailDtoItem.getOptionProductId())
                                .optionProductQuantity(orderDetailDtoItem.getOptionQuantity())
                                .build())) : new ArrayList<>())
                .build();
            productOrderDetailList.add(productOrderDetailRequest);
        }

        return PaymentOrderApproveRequestDto.builder()
            .orderTotalAmount(orderTotalAmount)
            .orderCode(orderCode)
            .clientId(null)
            .productOrderDetailList(productOrderDetailList)
            .build();
    }

    private PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto getProductOrderDetailRequestDto(
        Long productId, Long quantity, boolean usePackaging, Long optionProductId,
        Long optionQuantity) {
        return PaymentOrderApproveRequestDto.ProductOrderDetailRequestDto.builder()
            .productId(productId)
            .quantity(quantity)
            .productOrderDetailOptionRequestDtoList(usePackaging ? new ArrayList<>(
                List.of(PaymentOrderApproveRequestDto.ProductOrderDetailOptionRequestDto.builder()
                    .productId(optionProductId).optionProductQuantity(optionQuantity).build()))
                : new ArrayList<>())
            .build();
    }

    @Override
    public void changeOrderStatus(long orderId, String orderStatusKor) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        OrderStatus nextOrderStatus = OrderStatus.of(orderStatusKor);

        /*
         * [[ 주문 상태 변경 성공 시나리오 ]]
         * 1. 결제대기 -> 결제완료
         * 2. 결제완료 -> 배송중
         * 3. 배송중 -> 배송완료
         * 4. 결제대기 or 결제완료 -> 주문취소
         * 5. 배송중 or 배송완료 -> 반품 요청
         * 6. 반품요청 -> 반품
         */

        boolean canChange = (nextOrderStatus.equals(OrderStatus.PAYED) && order.getOrderStatus()
            .equals(OrderStatus.WAIT_PAYMENT)) ||
            (nextOrderStatus.equals(OrderStatus.DELIVERING) && order.getOrderStatus()
                .equals(OrderStatus.PAYED)) ||
            (nextOrderStatus.equals(OrderStatus.DELIVERY_COMPLETE)
                && order.getOrderStatus() == OrderStatus.DELIVERING) ||
            (nextOrderStatus.equals(OrderStatus.CANCEL) && (
                order.getOrderStatus().equals(OrderStatus.WAIT_PAYMENT) || order.getOrderStatus()
                    .equals(OrderStatus.PAYED))) ||
            (nextOrderStatus.equals(OrderStatus.REFUND_REQUEST) && (
                order.getOrderStatus().equals(OrderStatus.DELIVERING) || order.getOrderStatus()
                    .equals(OrderStatus.DELIVERY_COMPLETE))) ||
            (nextOrderStatus.equals(OrderStatus.REFUND) && order.getOrderStatus()
                .equals(OrderStatus.REFUND_REQUEST));

        if (canChange) {
            order.updateOrderStatus(nextOrderStatus);
        } else {
            throw new InvalidOrderChangeAttempt();
        }

        // 배송중으로 변경하면, 출고일 업데이트!
        if (nextOrderStatus == OrderStatus.DELIVERING) {
            order.updateDeliveryStartDate();
        }

        orderRepository.save(order);
    }

    @Override
    public Page<OrderResponseDto> getAllOrderList(int pageSize, int pageNo, String sortBy,
        String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
            Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Order> orderPage = orderRepository.findAll(PageRequest.of(pageNo, pageSize, sort));
        List<OrderResponseDto> responseDtoList = new ArrayList<>();

        for (Order order : orderPage.getContent()) {
            OrderResponseDto orderResponseDto = mapOrderToOrderResponseDto(order);
            List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder_OrderId(
                order.getOrderId());

            for (ProductOrderDetail productOrderDetail : orderDetailList) {
                OrderResponseDto.OrderListItem orderListItem = mapProductOrderDetailToOrderListItem(
                    productOrderDetail);
                orderResponseDto.addClientOrderListItem(orderListItem);
            }

            responseDtoList.add(orderResponseDto);
        }

        return new PageImpl<>(responseDtoList, PageRequest.of(pageNo, pageSize, sort),
            orderPage.getTotalElements());
    }

    private OrderResponseDto mapOrderToOrderResponseDto(Order order) {

        boolean isClientOrder = false;
        ClientOrder clientOrder = null;
        NonClientOrder nonClientOrder = null;

        if (Boolean.TRUE.equals(clientOrderRepository.existsByOrder_OrderId(order.getOrderId()))) {
            isClientOrder = true;
            clientOrder = clientOrderRepository.findByOrder_OrderId(order.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        } else {
            nonClientOrder = nonClientOrderRepository.findByOrder_OrderId(order.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        }

        return OrderResponseDto.builder()
            .orderId(order.getOrderId())
            .clientId(isClientOrder ? clientOrder.getClientId() : null)
            .couponId(isClientOrder ? clientOrder.getCouponId() : null)
            .orderCode(order.getOrderCode())
            .orderDatetime(order.getOrderDatetime().toString())
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
            .discountAmountByCoupon(isClientOrder ? clientOrder.getDiscountAmountByCoupon() : 0)
            .discountAmountByPoint(isClientOrder ? clientOrder.getDiscountAmountByPoint() : 0)
            .accumulatedPoint(isClientOrder ? clientOrder.getAccumulatedPoint() : 0)
            .nonClientOrdererEmail(isClientOrder ? null : nonClientOrder.getNonClientOrdererEmail())
            .nonClientOrdererName(isClientOrder ? null : nonClientOrder.getNonClientOrdererName())
            .nonClientOrderPassword(
                isClientOrder ? null : nonClientOrder.getNonClientOrderPassword())
            .build();
    }

    private OrderResponseDto.OrderListItem mapProductOrderDetailToOrderListItem(
        ProductOrderDetail productOrderDetail) {

        ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
            productOrderDetail.getProductOrderDetailId()).orElse(null);

        return OrderResponseDto.OrderListItem.builder()
            .productOrderDetailId(productOrderDetail.getProductOrderDetailId())
            .productId(productOrderDetail.getProductId())
            .productName(productOrderDetail.getProductName())
            .productQuantity(productOrderDetail.getQuantity())
            .productSinglePrice(productOrderDetail.getPricePerProduct())
            .optionProductId(
                productOrderDetailOption == null ? null : productOrderDetailOption.getProductId())
            .optionProductName(productOrderDetailOption == null ? null
                : productOrderDetailOption.getOptionProductName())
            .optionProductQuantity(
                productOrderDetailOption == null ? null : productOrderDetailOption.getQuantity())
            .optionProductSinglePrice(productOrderDetailOption == null ? null
                : productOrderDetailOption.getOptionProductPrice())
            .build();
    }


    @Override
    public List<ProductOrderDetailResponseDto> getProductOrderDetailList(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        List<ProductOrderDetail> productOrderDetailList = productOrderDetailRepository.findAllByOrder_OrderId(
            order.getOrderId());

        List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList = new ArrayList<>();

        for (ProductOrderDetail productOrderDetail : productOrderDetailList) {
            ProductOrderDetailResponseDto productOrderDetailResponseDto = ProductOrderDetailResponseDto.builder()
                .productOrderDetailId(productOrderDetail.getProductOrderDetailId())
                .orderId(order.getOrderId())
                .productId(productOrderDetail.getProductId())
                .quantity(productOrderDetail.getQuantity())
                .pricePerProduct(productOrderDetail.getPricePerProduct())
                .productName(productOrderDetail.getProductName())
                .build();
            productOrderDetailResponseDtoList.add(productOrderDetailResponseDto);
        }

        return productOrderDetailResponseDtoList;
    }

    @Override
    public ProductOrderDetailResponseDto getProductOrderDetail(Long orderId,
        Long productOrderDetailId) {

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        ProductOrderDetail productOrderDetail = productOrderDetailRepository.findById(
            productOrderDetailId).orElseThrow(ProductOrderDetailNotFoundException::new);

        return ProductOrderDetailResponseDto.builder()
            .productOrderDetailId(productOrderDetail.getProductOrderDetailId())
            .orderId(order.getOrderId())
            .productId(productOrderDetail.getProductId())
            .quantity(productOrderDetail.getQuantity())
            .pricePerProduct(productOrderDetail.getPricePerProduct())
            .productName(productOrderDetail.getProductName())
            .build();
    }

    @Override
    public ProductOrderDetailOptionResponseDto getProductOrderDetailOptionResponseDto(long orderId,
        long detailId) {

        ProductOrderDetail productOrderDetail = productOrderDetailRepository.findById(detailId)
            .orElseThrow(ProductOrderDetailNotFoundException::new);
        ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
                productOrderDetail.getProductOrderDetailId())
            .orElseThrow(ProductOrderDetailNotFoundException::new);

        return ProductOrderDetailOptionResponseDto.builder()
            .productId(productOrderDetailOption.getProductId())
            .productOrderDetailId(productOrderDetailOption.getProductOrderDetailOptionId())
            .optionProductName(productOrderDetailOption.getOptionProductName())
            .optionProductPrice(productOrderDetailOption.getOptionProductPrice())
            .optionProductQuantity(productOrderDetailOption.getQuantity())
            .build();
    }

}
