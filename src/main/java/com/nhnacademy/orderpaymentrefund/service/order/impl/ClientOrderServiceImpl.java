package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.client.TestOtherService;
import com.nhnacademy.orderpaymentrefund.converter.impl.ClientOrderConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverterImpl;
import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.ClientOrderFormRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailOptionResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ProductOrderDetailResponseDto;
import com.nhnacademy.orderpaymentrefund.exception.CannotCancelOrder;
import com.nhnacademy.orderpaymentrefund.exception.OrderNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.ProductOrderDetailNotFoundException;
import com.nhnacademy.orderpaymentrefund.exception.WrongClientAccessToOrder;
import com.nhnacademy.orderpaymentrefund.exception.type.BadRequestExceptionType;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailOptionRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.ProductOrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import com.nhnacademy.orderpaymentrefund.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    // orderService(공통)
    private final OrderService orderService;

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
    public Page<ClientOrderGetResponseDto> getOrders(HttpHeaders headers, int pageSize, int pageNo, String sortBy, String sortDir) {

        long clientId = getClientId(headers);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Page<Order> orderPage = orderRepository.findByClientId(clientId, PageRequest.of(pageNo, pageSize, sort));

        List<ClientOrderGetResponseDto> responseDtoList = new ArrayList<>();

        for(Order order : orderPage.getContent()){

            if(!order.getClientId().equals(clientId)) throw new WrongClientAccessToOrder();

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
                    .designatedDeliveryDate(order.getDesignatedDeliveryDate() == null ? null : order.getDesignatedDeliveryDate().toString())
                    .deliveryStartDate(order.getDeliveryStartDate() == null ? null : order.getDeliveryStartDate().toString())
                    .phoneNumber(order.getPhoneNumber())
                    .deliveryAddress(order.getDeliveryAddress())
                    .discountAmountByCoupon(order.getDiscountAmountByCoupon())
                    .discountAmountByPoint(order.getDiscountAmountByPoint())
                    .accumulatedPoint(order.getAccumulatedPoint())
                    .build();

            List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder(order);

            for(ProductOrderDetail productOrderDetail : orderDetailList){

                ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail(productOrderDetail);

                ClientOrderGetResponseDto.ClientProductOrderDetailListItem clientProductOrderDetailListItem =
                        ClientOrderGetResponseDto.ClientProductOrderDetailListItem.builder()
                                .productOrderDetailId(productOrderDetail.getProductOrderDetailId())
                                .productId(productOrderDetail.getProductId())
                                .productName(productOrderDetail.getProductName())
                                .productQuantity(productOrderDetail.getQuantity())
                                .productSinglePrice(productOrderDetail.getPricePerProduct())
                                .optionProductId(productOrderDetailOption == null ? null : productOrderDetailOption.getProductId())
                                .optionProductName(productOrderDetailOption == null ? null : productOrderDetailOption.getOptionProductName())
                                .optionProductQuantity(productOrderDetailOption == null ? null : productOrderDetailOption.getQuantity())
                                .optionProductSinglePrice(productOrderDetailOption == null ? null : productOrderDetailOption.getOptionProductPrice())
                                .build();

                clientOrderGetResponseDto.addClientProductOrderDetailListItem(clientProductOrderDetailListItem);

            }

            responseDtoList.add(clientOrderGetResponseDto);

        }

        return new PageImpl<>(responseDtoList, PageRequest.of(pageNo, pageSize, sort), orderPage.getTotalElements());

    }

    @Override
    public ClientOrderGetResponseDto getOrder(HttpHeaders headers, long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) throw new WrongClientAccessToOrder();

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
                .designatedDeliveryDate(order.getDesignatedDeliveryDate() == null ? null : order.getDesignatedDeliveryDate().toString())
                .deliveryStartDate(order.getDeliveryStartDate() == null ? null : order.getDeliveryStartDate().toString())
                .phoneNumber(order.getPhoneNumber())
                .deliveryAddress(order.getDeliveryAddress())
                .discountAmountByCoupon(order.getDiscountAmountByCoupon())
                .discountAmountByPoint(order.getDiscountAmountByPoint())
                .accumulatedPoint(order.getAccumulatedPoint())
                .build();

        List<ProductOrderDetail> orderDetailList = productOrderDetailRepository.findAllByOrder(order);

        for(ProductOrderDetail productOrderDetail : orderDetailList){

            ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail(productOrderDetail);

            ClientOrderGetResponseDto.ClientProductOrderDetailListItem clientProductOrderDetailListItem =
                    ClientOrderGetResponseDto.ClientProductOrderDetailListItem.builder()
                            .productOrderDetailId(productOrderDetail.getProductOrderDetailId())
                            .productId(productOrderDetail.getProductId())
                            .productName(productOrderDetail.getProductName())
                            .productQuantity(productOrderDetail.getQuantity())
                            .productSinglePrice(productOrderDetail.getPricePerProduct())
                            .optionProductId(productOrderDetailOption == null ? null : productOrderDetailOption.getProductId())
                            .optionProductName(productOrderDetailOption == null ? null : productOrderDetailOption.getOptionProductName())
                            .optionProductQuantity(productOrderDetailOption == null ? null : productOrderDetailOption.getQuantity())
                            .optionProductSinglePrice(productOrderDetailOption == null ? null : productOrderDetailOption.getOptionProductPrice())
                            .build();

            clientOrderGetResponseDto.addClientProductOrderDetailListItem(clientProductOrderDetailListItem);

        }

        return clientOrderGetResponseDto;

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

    @Override
    public List<ProductOrderDetailResponseDto> getProductOrderDetailList(HttpHeaders headers, Long orderId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        List<ProductOrderDetail> productOrderDetailList = productOrderDetailRepository.findAllByOrder(order);

        List<ProductOrderDetailResponseDto> productOrderDetailResponseDtoList = new ArrayList<>();

        for(ProductOrderDetail productOrderDetail : productOrderDetailList){
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
    public ProductOrderDetailResponseDto getProductOrderDetail(HttpHeaders headers, Long orderId, Long productOrderDetailId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        ProductOrderDetail productOrderDetail = productOrderDetailRepository.findById(productOrderDetailId).orElseThrow(ProductOrderDetailNotFoundException::new);

        if(productOrderDetail.getOrder().equals(order)){
            throw new BadRequestExceptionType("orderId와 productOrderDetailId가 매칭되지 않습니다");
        }

        return ProductOrderDetailResponseDto.builder()
                .orderId(order.getOrderId())
                .productId(productOrderDetail.getProductId())
                .quantity(productOrderDetail.getQuantity())
                .pricePerProduct(productOrderDetail.getPricePerProduct())
                .productName(productOrderDetail.getProductName())
                .build();

    }

    @Override
    public ProductOrderDetailOptionResponseDto getProductOrderDetailOptionResponseDto(HttpHeaders headers, long orderId, long detailId) {

        long clientId = getClientId(headers);

        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if(!order.getClientId().equals(clientId)) {
            throw new WrongClientAccessToOrder();
        }

        ProductOrderDetail productOrderDetail = productOrderDetailRepository.findById(detailId).orElseThrow(ProductOrderDetailNotFoundException::new);
        ProductOrderDetailOption productOrderDetailOption = productOrderDetailOptionRepository.findFirstByProductOrderDetail(productOrderDetail);

        if(productOrderDetail.getOrder().equals(order)){
            throw new BadRequestExceptionType("orderId와 productOrderDetailId가 매칭되지 않습니다");
        }

        return ProductOrderDetailOptionResponseDto.builder()
                .productId(productOrderDetailOption.getProductId())
                .productOrderDetailId(productOrderDetailOption.getProductOrderDetailOptionId())
                .optionProductName(productOrderDetailOption.getOptionProductName())
                .optionProductPrice(productOrderDetailOption.getOptionProductPrice())
                .optionProductQuantity(productOrderDetailOption.getQuantity())
                .build();
    }

    private long getClientId(HttpHeaders headers){
        if (headers.get(ID_HEADER) == null){
            throw new RuntimeException("clientId is null");
        }
        return Long.parseLong(headers.getFirst(ID_HEADER));
    }
}
