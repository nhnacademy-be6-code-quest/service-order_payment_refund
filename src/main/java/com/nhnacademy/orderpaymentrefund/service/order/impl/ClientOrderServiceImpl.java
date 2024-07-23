package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderCreateForm;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelOrder;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.ProductOrderDetailNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.WrongClientAccessToOrder;
import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;
import com.nhnacademy.orderpaymentrefund.exception.type.UnauthorizedExceptionType;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private final ProductOrderDetailRepository productOrderDetailRepository;
    private final ProductOrderDetailOptionRepository productOrderDetailOptionRepository;
    private final OrderRepository orderRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void saveClientTemporalOrder(HttpHeaders headers, ClientOrderCreateForm requestDto) {
        String tossOrderId = requestDto.getTossOrderId();
        redisTemplate.opsForHash().put("order", tossOrderId, requestDto);
    }


    @Override
    public ClientOrderCreateForm getClientTemporalOrder(HttpHeaders headers, String tossOrderId) {

        ClientOrderCreateForm clientOrderCreateForm = (ClientOrderCreateForm) redisTemplate.opsForHash()
            .get("order", tossOrderId);

        assert clientOrderCreateForm != null;

        return clientOrderCreateForm;
    }

    @Override
    public Page<ClientOrderGetResponseDto> getOrders(HttpHeaders headers, int pageSize, int pageNo,
        String sortBy, String sortDir) {
        long clientId = getClientId(headers);
        Sort sort = getSortOrder(sortBy, sortDir);
        Page<Order> orderPage = fetchOrders(clientId, pageSize, pageNo, sort);

        List<ClientOrderGetResponseDto> responseDtoList = new ArrayList<>();
        for (Order order : orderPage.getContent()) {
            responseDtoList.add(mapToClientOrderGetResponseDto(order));
        }
        return new PageImpl<>(responseDtoList, PageRequest.of(pageNo, pageSize, sort),
            responseDtoList.size());
    }

    private Sort getSortOrder(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();
    }

    private Page<Order> fetchOrders(long clientId, int pageSize, int pageNo, Sort sort) {
        return orderRepository.findByClientId(clientId, PageRequest.of(pageNo, pageSize, sort));
    }

    private ClientOrderGetResponseDto mapToClientOrderGetResponseDto(Order order) {
        ClientOrderGetResponseDto dto = ClientOrderGetResponseDto.builder()
            .orderId(order.getOrderId())
            .clientId(order.getClientId())
            .couponId(order.getCouponId())
            .tossOrderId(order.getTossOrderId())
            .orderDatetime(String.valueOf(order.getOrderDatetime()))
            .orderStatus(order.getOrderStatus().kor)
            .productTotalAmount(order.getProductTotalAmount())
            .shippingFee(order.getShippingFee())
            .orderTotalAmount(order.getOrderTotalAmount())
            .designatedDeliveryDate(formatDate(order.getDesignatedDeliveryDate()))
            .deliveryStartDate(formatDate(order.getDeliveryStartDate()))
            .phoneNumber(order.getPhoneNumber())
            .deliveryAddress(order.getDeliveryAddress())
            .discountAmountByCoupon(order.getDiscountAmountByCoupon())
            .discountAmountByPoint(order.getDiscountAmountByPoint())
            .accumulatedPoint(order.getAccumulatedPoint())
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
        List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder(
            order);
        return orderDetailList.stream()
            .map(this::mapToClientProductOrderDetailListItem)
            .toList();
    }

    private ClientOrderGetResponseDto.ClientProductOrderDetailListItem mapToClientProductOrderDetailListItem(
        ProductOrderDetail productOrderDetail) {
        ProductOrderDetailOption option = productOrderDetailOptionRepository.findFirstByProductOrderDetail(
            productOrderDetail).orElseThrow();

        return getClientProductOrderDetailListItem(productOrderDetail, option);
    }

    @Override
    public ClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        assert order.getClientId() != null;
        if (!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        ClientOrderGetResponseDto clientOrderGetResponseDto = ClientOrderGetResponseDto.builder()
            .orderId(order.getOrderId())
            .clientId(order.getClientId())
            .couponId(order.getCouponId())
            .tossOrderId(order.getTossOrderId())
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
            .discountAmountByCoupon(order.getDiscountAmountByCoupon())
            .discountAmountByPoint(order.getDiscountAmountByPoint())
            .accumulatedPoint(order.getAccumulatedPoint())
            .build();

        List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder(
            order);

        for (ProductOrderDetail productOrderDetail : orderDetailList) {

            ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail(
                productOrderDetail).orElseThrow();

            ClientOrderGetResponseDto.ClientProductOrderDetailListItem clientProductOrderDetailListItem = getClientProductOrderDetailListItem(
                productOrderDetail, productOrderDetailOption);

            clientOrderGetResponseDto.addClientProductOrderDetailListItem(
                clientProductOrderDetailListItem);

        }

        return clientOrderGetResponseDto;

    }

    @Override
    public void cancelOrder(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        assert order.getClientId() != null;
        if (!order.getClientId().equals(clientId)) {
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

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        assert order.getClientId() != null;
        if (!order.getClientId().equals(clientId)) {
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

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        assert order.getClientId() != null;
        if (!order.getClientId().equals(clientId)) {
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
    public String getOrderStatus(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        assert order.getClientId() != null;
        if (!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        return order.getOrderStatus().kor;

    }

    @Override
    public List<ProductOrderDetailResponseDto> getProductOrderDetailResponseDtoList(
        HttpHeaders headers, Long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        assert order.getClientId() != null;
        if (!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        List<ProductOrderDetail> productOrderDetailList = productOrderDetailRepository.findAllByOrder(
            order);

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

        Order order = getOrderEntity(headers, orderId);

        ProductOrderDetail productOrderDetail = getProductOrderDetailEntity(productOrderDetailId);

        if (!productOrderDetail.getOrder().equals(order)) {
            throw new BadRequestExceptionType("orderId와 productOrderDetailId가 매칭되지 않습니다");
        }

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

        ProductOrderDetail productOrderDetail = getProductOrderDetailEntity(detailId);
        ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail(
            productOrderDetail).orElseThrow(ProductOrderDetailNotFoundException::new);

        return ProductOrderDetailOptionResponseDto.builder()
            .productId(productOrderDetailOption.getProductId())
            .productOrderDetailId(productOrderDetailOption.getProductOrderDetailOptionId())
            .optionProductName(productOrderDetailOption.getOptionProductName())
            .optionProductPrice(productOrderDetailOption.getOptionProductPrice())
            .optionProductQuantity(productOrderDetailOption.getQuantity())
            .build();
    }

    private long getClientId(HttpHeaders headers) {
        if (headers.get(ID_HEADER) == null) {
            throw new UnauthorizedExceptionType("clientId is null");
        }
        return Long.parseLong(Objects.requireNonNull(headers.getFirst(ID_HEADER)));
    }

    private ProductOrderDetail getProductOrderDetailEntity(Long productOrderDetailId) {
        return productOrderDetailRepository.findById(productOrderDetailId)
            .orElseThrow(ProductOrderDetailNotFoundException::new);
    }

    private Order getOrderEntity(HttpHeaders headers, Long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        assert order.getClientId() != null;
        if (!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        return order;

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
