package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.client.coupon.CouponClient;
import com.nhnacademy.orderpaymentrefund.context.ClientHeaderContext;
import com.nhnacademy.orderpaymentrefund.domain.order.ClientOrder;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.CouponOrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderCouponDiscountInfo;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelOrder;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.ProductOrderDetailNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.WrongClientAccessToOrder;
import com.nhnacademy.orderpaymentrefund.repository.order.ClientOrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {

    private static final String ID_HEADER = "X-User-Id";

    private final ClientHeaderContext clientHeaderContext;

    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final OrderRepository orderRepository;
    private final ClientOrderRepository clientOrderRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final CouponClient couponClient;

    private static final String AMOUNTDISCOUNT = "AMOUNTDISCOUNT";
    private static final String PERCENTAGEDISCOUNT = "PERCENTAGEDISCOUNT";

    @Override
    public void saveClientTemporalOrder(HttpHeaders headers, ClientOrderCreateForm requestDto) {
        String orderCode = requestDto.getOrderCode();
        redisTemplate.opsForHash().put("order", orderCode, requestDto);
    }

    @Override
    public ClientOrderCreateForm getClientTemporalOrder(HttpHeaders headers, String orderCode) {

        ClientOrderCreateForm clientOrderCreateForm = (ClientOrderCreateForm) redisTemplate.opsForHash()
            .get("order", orderCode);

        assert clientOrderCreateForm != null;

        return clientOrderCreateForm;
    }

    @Override
    public Page<ClientOrderGetResponseDto> getOrders(HttpHeaders headers, int pageSize, int pageNo,
        String sortBy, String sortDir) {

        Sort sort = getSortOrder(sortBy, sortDir);

        Page<ClientOrder> clientOrderPage = clientOrderRepository.findByClientId(
            clientHeaderContext.getClientId(),
            PageRequest.of(pageNo, pageSize, sort));

        List<ClientOrderGetResponseDto> responseDtoList = new ArrayList<>();
        for (ClientOrder clientOrder : clientOrderPage.getContent()) {
            responseDtoList.add(mapToClientOrderGetResponseDto(clientOrder));
        }
        return new PageImpl<>(responseDtoList, PageRequest.of(pageNo, pageSize, sort),
            responseDtoList.size());
    }

    private Sort getSortOrder(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
    }

    private ClientOrderGetResponseDto mapToClientOrderGetResponseDto(ClientOrder clientOrder) {

        Order order = clientOrder.getOrder();

        ClientOrderGetResponseDto dto = ClientOrderGetResponseDto.builder()
            .orderId(order.getOrderId())
            .clientId(clientOrder.getClientId())
            .couponId(clientOrder.getCouponId())
            .orderCode(order.getOrderCode())
            .orderDatetime(String.valueOf(order.getOrderDatetime()))
            .orderStatus(order.getOrderStatus().kor)
            .productTotalAmount(order.getProductTotalAmount())
            .shippingFee(order.getShippingFee())
            .orderTotalAmount(order.getOrderTotalAmount())
            .designatedDeliveryDate(formatDate(order.getDesignatedDeliveryDate()))
            .deliveryStartDate(formatDate(order.getDeliveryStartDate()))
            .phoneNumber(order.getPhoneNumber())
            .deliveryAddress(order.getDeliveryAddress())
            .discountAmountByCoupon(clientOrder.getDiscountAmountByCoupon())
            .discountAmountByPoint(clientOrder.getDiscountAmountByPoint())
            .accumulatedPoint(clientOrder.getAccumulatedPoint())
            .build();

        List<ClientOrderGetResponseDto.ClientProductOrderDetailListItem> detailListItems = getOrderDetails(
            order);
        detailListItems.forEach(dto::addClientProductOrderDetailListItem);

        return dto;
    }

    private String formatDate(LocalDate date) {
        return date == null ? null : date.toString();
    }

    private List<ClientOrderGetResponseDto.ClientProductOrderDetailListItem> getOrderDetails(
        Order order) {
        List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder_OrderId(
            order.getOrderId());
        return orderDetailList.stream()
            .map(this::mapToClientProductOrderDetailListItem)
            .toList();
    }

    private ClientOrderGetResponseDto.ClientProductOrderDetailListItem mapToClientProductOrderDetailListItem(
        ProductOrderDetail productOrderDetail) {
        ProductOrderDetailOption option = productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
            productOrderDetail.getProductOrderDetailId()).orElse(null);

        return getClientProductOrderDetailListItem(productOrderDetail, option);
    }

    @Override
    public ClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId) {

        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(orderId).orElseThrow(OrderNotFoundException::new);
        Order order = clientOrder.getOrder();

        if (!clientHeaderContext.getClientId().equals(clientOrder.getClientId())) {
            throw new WrongClientAccessToOrder();
        }

        ClientOrderGetResponseDto clientOrderGetResponseDto = ClientOrderGetResponseDto.builder()
            .orderId(order.getOrderId())
            .clientId(clientOrder.getClientOrderId())
            .couponId(clientOrder.getCouponId())
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
            .discountAmountByCoupon(clientOrder.getDiscountAmountByCoupon())
            .discountAmountByPoint(clientOrder.getDiscountAmountByPoint())
            .accumulatedPoint(clientOrder.getAccumulatedPoint())
            .build();

        List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder_OrderId(
            orderId);

        for (ProductOrderDetail productOrderDetail : orderDetailList) {

            ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
                productOrderDetail.getProductOrderDetailId()).orElseThrow();

            ClientOrderGetResponseDto.ClientProductOrderDetailListItem clientProductOrderDetailListItem = getClientProductOrderDetailListItem(
                productOrderDetail, productOrderDetailOption);

            clientOrderGetResponseDto.addClientProductOrderDetailListItem(
                clientProductOrderDetailListItem);

        }

        return clientOrderGetResponseDto;

    }

    @Override
    public List<OrderCouponDiscountInfo> getCouponDiscountInfoList(HttpHeaders headers, ClientOrderCreateForm clientOrderCreateForm) {

        List<CouponOrderResponseDto> couponList = couponClient.findClientCoupon(headers);

        List<OrderCouponDiscountInfo> couponDiscountInfoList = new ArrayList<>();

        for(CouponOrderResponseDto coupon : couponList) {
            OrderCouponDiscountInfo orderCouponDiscountInfo = orderCouponDiscountInfo(coupon, clientOrderCreateForm);
            couponDiscountInfoList.add(orderCouponDiscountInfo);
        }

        return couponDiscountInfoList;
    }

    private OrderCouponDiscountInfo orderCouponDiscountInfo(CouponOrderResponseDto coupon, ClientOrderCreateForm clientOrderCreateForm) {

        log.info("쿠폰 할인 정보 계산");

        List<OrderDetailDtoItem> orderDetailDtoItemList = clientOrderCreateForm.getOrderDetailDtoItemList();

        Long totalQuantity = 0L;
        for(OrderDetailDtoItem orderDetailDtoItem : clientOrderCreateForm.getOrderDetailDtoItemList()){
            totalQuantity += (orderDetailDtoItem.getQuantity() + orderDetailDtoItem.getOptionQuantity());
        }

        if (isAmountDiscount(coupon)) {
            return getOrderCouponDiscountInfoUsingAmountDiscount(
                coupon, clientOrderCreateForm.getProductTotalAmount(),
                totalQuantity, orderDetailDtoItemList,
                "전체 상품 주문 금액이 최소 주문 금액에 못 미칩니다"
            );
        }

        if (isPercentageDiscount(coupon)) {
            return getOrderCouponDiscountInfoUsingPercentageDiscount(
                coupon, clientOrderCreateForm.getProductTotalAmount(),
                totalQuantity, orderDetailDtoItemList,
                "전체 상품 주문 금액이 최소 주문 금액에 못 미칩니다"
            );
        }

        if (coupon.getProductCoupon() != null) {
            return processProductCoupon(coupon, orderDetailDtoItemList);
        }

        if (coupon.getCategoryCoupon() != null) {
            return processCategoryCoupon(coupon, orderDetailDtoItemList);
        }

        return createNotApplicableOrderCouponDiscountInfo(coupon, "쿠폰을 적용할 수 없습니다");
    }

    private boolean isAmountDiscount(CouponOrderResponseDto coupon) {
        return coupon.getProductCoupon() == null && coupon.getCategoryCoupon() == null &&
            coupon.getCouponPolicyDto() != null &&
            coupon.getCouponPolicyDto().getDiscountType().equals(AMOUNTDISCOUNT);
    }

    private boolean isPercentageDiscount(CouponOrderResponseDto coupon) {
        return coupon.getProductCoupon() == null && coupon.getCategoryCoupon() == null &&
            coupon.getCouponPolicyDto() != null &&
            coupon.getCouponPolicyDto().getDiscountType().equals(PERCENTAGEDISCOUNT);
    }

    private OrderCouponDiscountInfo processProductCoupon(CouponOrderResponseDto coupon, List<OrderDetailDtoItem> orderDetailDtoItemList) {
        Long applicableProductId = coupon.getProductCoupon().getProductId();

        for (OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList) {
            if (orderDetailDtoItem.getProductId().equals(applicableProductId)) {
                if (coupon.getCouponPolicyDto().getDiscountType().equals(AMOUNTDISCOUNT)) {
                    return getOrderCouponDiscountInfoUsingAmountDiscount(coupon,
                        orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity(),
                        orderDetailDtoItem.getQuantity(), List.of(orderDetailDtoItem),
                        "쿠폰 적용가능한 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
                }
                if (coupon.getCouponPolicyDto().getDiscountType().equals(PERCENTAGEDISCOUNT)) {
                    return getOrderCouponDiscountInfoUsingPercentageDiscount(coupon,
                        orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity(),
                        orderDetailDtoItem.getQuantity(), List.of(orderDetailDtoItem),
                        "쿠폰 적용가능한 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
                }
            }
        }

        return createNotApplicableOrderCouponDiscountInfo(coupon, "쿠폰을 적용할 수 있는 상품을 주문하지 않았습니다");
    }

    private OrderCouponDiscountInfo processCategoryCoupon(CouponOrderResponseDto coupon, List<OrderDetailDtoItem> orderDetailDtoItemList) {
        Long applicableCategoryId = coupon.getCategoryCoupon().getProductCategoryId();
        List<OrderDetailDtoItem> applicableProduct = new ArrayList<>();
        long totalQuantity = 0;
        long totalPrice = 0;

        for (OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList) {
            if (orderDetailDtoItem.getCategoryIdList() == null) break;
            if (orderDetailDtoItem.getCategoryIdList().contains(applicableCategoryId)) {
                applicableProduct.add(orderDetailDtoItem);
                totalQuantity += orderDetailDtoItem.getQuantity();
                totalPrice += orderDetailDtoItem.getProductSinglePrice() * orderDetailDtoItem.getQuantity();
            }
        }

        if (!applicableProduct.isEmpty()) {
            if (coupon.getCouponPolicyDto().getDiscountType().equals(AMOUNTDISCOUNT)) {
                return getOrderCouponDiscountInfoUsingAmountDiscount(coupon, totalPrice, totalQuantity, applicableProduct, "쿠폰 적용가능한 카테고리 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
            }
            if (coupon.getCouponPolicyDto().getDiscountType().equals(PERCENTAGEDISCOUNT)) {
                return getOrderCouponDiscountInfoUsingPercentageDiscount(coupon, totalPrice, totalQuantity, applicableProduct, "쿠폰 적용가능한 카테고리 상품의 총 주문 금액이 최소 주문 금액에 못 미칩니다");
            }
        }

        return createNotApplicableOrderCouponDiscountInfo(coupon, "쿠폰을 적용할 수 있는 카테고리 상품을 구매하지 않았습니다");
    }

    private OrderCouponDiscountInfo createNotApplicableOrderCouponDiscountInfo(CouponOrderResponseDto coupon, String description) {
        OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
            .couponId(coupon.getCouponId())
            .isApplicable(false)
            .build();
        orderCouponDiscountInfo.updateNotApplicableDescription(description);
        return orderCouponDiscountInfo;
    }


    // coupon: 적용할 쿠폰
    // productTotalAmount: 쿠폰의 '최소 주문 금액'을 체크하기 위한 총 액수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 액수가 됨.
    // totalQuantity: 쿠폰을 적용시켜 할인 받을 상품의 총 개수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 수량이 됨.
    // orderDetailDtoItemList: 쿠폰 적용 대상이 되는 상품들.
    private OrderCouponDiscountInfo getOrderCouponDiscountInfoUsingAmountDiscount(CouponOrderResponseDto coupon, Long productTotalAmount, Long totalQuantity, List<OrderDetailDtoItem> orderDetailDtoItemList, String updateNotApplicableDescription){

        // 최소 금액 기준이 맞지 않음 => 쿠폰 사용 불가.
        if(productTotalAmount < coupon.getCouponPolicyDto().getMinPurchaseAmount()){
            OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                .couponId(coupon.getCouponId())
                .isApplicable(false)
                .build();
            orderCouponDiscountInfo.updateNotApplicableDescription(updateNotApplicableDescription);
            return orderCouponDiscountInfo;
        }

        // 쿠폰 할인 금액
        long discountValue = coupon.getCouponPolicyDto().getDiscountValue();

        // 각 상품 당 할인 금액
        long discountValuePerProduct = Math.round((double) discountValue / totalQuantity);

        OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
            .couponId(coupon.getCouponId())
            .isApplicable(true)
            .discountTotalAmount(discountValue)
            .build();

        for(OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList){
            orderCouponDiscountInfo.addProductPriceInfo(orderDetailDtoItem.getProductId(), orderDetailDtoItem.getProductSinglePrice() - discountValuePerProduct);
        }

        return orderCouponDiscountInfo;

    }

    // coupon: 적용할 쿠폰
    // productTotalAmount: 쿠폰의 '최소 주문 금액'을 체크하기 위한 총 액수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 액수가 됨.
    // totalQuantity: 쿠폰을 적용시켜 할인 받을 상품의 총 개수. 상품 쿠폰 Or 카테고리 쿠폰의 경우, 적용할 상품들의 총 수량이 됨.
    // orderDetailDtoItemList: 쿠폰 적용 대상이 되는 상품들.
    private OrderCouponDiscountInfo getOrderCouponDiscountInfoUsingPercentageDiscount(CouponOrderResponseDto coupon, Long productTotalAmount, Long totalQuantity,

        List<OrderDetailDtoItem> orderDetailDtoItemList, String updateNotApplicableDescription) {

        // 최소 금액 기준이 맞지 않음 => 쿠폰 사용 불가.
        if (productTotalAmount < coupon.getCouponPolicyDto().getMinPurchaseAmount()) {
            OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
                .couponId(coupon.getCouponId())
                .isApplicable(false)
                .build();
            orderCouponDiscountInfo.updateNotApplicableDescription(updateNotApplicableDescription);
            return orderCouponDiscountInfo;
        }

        // 할인금액 계산
        long discountValue = Math.round(
            productTotalAmount * (0.01 * coupon.getCouponPolicyDto().getDiscountValue()));
        // 할인 금액이 쿠폰의 '최대 할인 금액'을 초과할 경우.
        if (coupon.getCouponPolicyDto().getMaxDiscountAmount() < discountValue) {
            discountValue = coupon.getCouponPolicyDto().getMaxDiscountAmount();
        }
        // 상품 별 할인 금액
        long discountValuePerProduct = Math.round((double) discountValue / totalQuantity);
        discountValue = discountValuePerProduct * totalQuantity;

        OrderCouponDiscountInfo orderCouponDiscountInfo = OrderCouponDiscountInfo.builder()
            .couponId(coupon.getCouponId())
            .isApplicable(true)
            .discountTotalAmount(discountValue)
            .build();

        for (OrderDetailDtoItem orderDetailDtoItem : orderDetailDtoItemList) {
            orderCouponDiscountInfo.addProductPriceInfo(orderDetailDtoItem.getProductId(),
                orderDetailDtoItem.getProductSinglePrice() - discountValuePerProduct);
        }

        return orderCouponDiscountInfo;
    }




    @Override
    public void cancelOrder(HttpHeaders headers, long orderId) {

        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(orderId).orElseThrow(OrderNotFoundException::new);
        Order order = clientOrder.getOrder();

        if (!clientHeaderContext.getClientId().equals(clientOrder.getClientId())) {
            throw new WrongClientAccessToOrder();
        }

        if (!(order.getOrderStatus() == OrderStatus.WAIT_PAYMENT
            || order.getOrderStatus() == OrderStatus.PAYED)) {
            log.info("유효하지 않은 상태에서 주문취소 상태 변경을 시도하고 있습니다.");
            throw new CannotCancelOrder("결제대기 또는 결제완료 상태에서 주문취소 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.CANCEL);
        orderRepository.save(order);

        log.info("order 주문 취소 상태 변경 성공");

    }

    @Override
    public void refundOrder(HttpHeaders headers, long orderId) {

        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(orderId).orElseThrow(OrderNotFoundException::new);
        Order order = clientOrder.getOrder();

        if (!clientHeaderContext.getClientId().equals(clientOrder.getClientId())) {
            throw new WrongClientAccessToOrder();
        }

        if (order.getOrderStatus() != OrderStatus.REFUND_REQUEST) {
            throw new CannotCancelOrder("반품 요청 상태에서 반품 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.REFUND);
        orderRepository.save(order);

    }

    @Override
    public void refundOrderRequest(HttpHeaders headers, long orderId) {

        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(orderId).orElseThrow(OrderNotFoundException::new);
        Order order = clientOrder.getOrder();

        if (!clientHeaderContext.getClientId().equals(clientOrder.getClientId())) {
            throw new WrongClientAccessToOrder();
        }

        if (!(order.getOrderStatus() == OrderStatus.DELIVERING
            || order.getOrderStatus() == OrderStatus.DELIVERY_COMPLETE)) {
            throw new CannotCancelOrder("배송중 또는 배송완료 상태에서 주문반품요청 가능합니다.");
        }

        order.updateOrderStatus(OrderStatus.REFUND_REQUEST);
        orderRepository.save(order);

    }

    @Override
    public List<ProductOrderDetailResponseDto> getProductOrderDetailResponseDtoList(
        HttpHeaders headers, Long orderId) {

        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(orderId).orElseThrow(OrderNotFoundException::new);
        Order order = clientOrder.getOrder();

        if (!clientHeaderContext.getClientId().equals(clientOrder.getClientId())) {
            throw new WrongClientAccessToOrder();
        }

        List<ProductOrderDetail> productOrderDetailList = productOrderDetailRepository.findAllByOrder_OrderId(
            orderId);

        List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList = new ArrayList<>();

        for (ProductOrderDetail productOrderDetail : productOrderDetailList) {
            ProductOrderDetailResponseDto productOrderDetailResponseDto = ProductOrderDetailResponseDto.builder()
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
    public ProductOrderDetailResponseDto getProductOrderDetailResponseDto(HttpHeaders headers,
        Long orderId, Long productOrderDetailId) {

        ClientOrder clientOrder = clientOrderRepository.findByOrder_OrderId(orderId).orElseThrow(OrderNotFoundException::new);
        Order order = clientOrder.getOrder();

        if (!clientHeaderContext.getClientId().equals(clientOrder.getClientId())) {
            throw new WrongClientAccessToOrder();
        }

        ProductOrderDetail productOrderDetail = productOrderDetailRepository.findById(productOrderDetailId)
            .orElseThrow(ProductOrderDetailNotFoundException::new);

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
    public ProductOrderDetailOptionResponseDto getProductOrderDetailOptionResponseDto(
        HttpHeaders headers, long orderId, long detailId) {

        ProductOrderDetail productOrderDetail = productOrderDetailRepository.findById(detailId)
            .orElseThrow(ProductOrderDetailNotFoundException::new);
        ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail_ProductOrderDetailId(
            productOrderDetail.getProductOrderDetailId()).orElseThrow(ProductOrderDetailNotFoundException::new);

        return ProductOrderDetailOptionResponseDto.builder()
            .productId(productOrderDetailOption.getProductId())
            .productOrderDetailId(productOrderDetailOption.getProductOrderDetailOptionId())
            .optionProductName(productOrderDetailOption.getOptionProductName())
            .optionProductPrice(productOrderDetailOption.getOptionProductPrice())
            .optionProductQuantity(productOrderDetailOption.getQuantity())
            .build();
    }

    private ClientOrderGetResponseDto.ClientProductOrderDetailListItem getClientProductOrderDetailListItem(
        ProductOrderDetail productOrderDetail, ProductOrderDetailOption productOrderDetailOption) {
        return ClientOrderGetResponseDto.ClientProductOrderDetailListItem.builder()
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

}
