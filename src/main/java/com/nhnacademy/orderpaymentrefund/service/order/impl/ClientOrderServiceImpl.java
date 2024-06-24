package com.nhnacademy.orderpaymentrefund.service.order.impl;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.request.client.ClientViewOrderPostRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientOrderPostResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.client.ClientViewOrderPostResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.field.*;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderDetailRepository;
import com.nhnacademy.orderpaymentrefund.repository.order.OrderRepository;
import com.nhnacademy.orderpaymentrefund.service.order.ClientOrderService;
import com.nhnacademy.orderpaymentrefund.service.shipping.ShippingPolicyService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientOrderServiceImpl implements ClientOrderService {

    private ShippingPolicyService shippingPolicyService;
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    @Override
    // TODO 각 서비스의 api 문서가 나와야 완성 가능. 현재, 해당 프로젝트에서 끌어올 수 없는 데이터는 그냥 임의로 생성!
    // TODO 임의로 생성한 데이터 클래스에서 AllArgs 애노테이션 삭제하기
    public ClientViewOrderPostResponseDto orderView(long clientId, ClientViewOrderPostRequestDto clientOrderPostRequestDto) {

        // 우리 서비스에서 가져올 수 있는 데이터들
        ShippingPolicy shippingPolicy = shippingPolicyService.getShippingPolicy();

        int shippingFee = shippingPolicy.getFee();
        int minPurchasePrice = shippingPolicy.getLowerBound();
        String shippingPolicyName = shippingPolicy.getDescription();


        // feinclient를 통해 가져올 데이터들
        List<ProductItemDto> productItemDtoList = List.of(
                new ProductItemDto(1, "/upload/book/이것이자바다.jpg", "이것이자바다!", 10000, 1),
                new ProductItemDto(2, "/upload/book/이것이자바니.jpg", "이것이자바니?", 15000, 3),
                new ProductItemDto(3, "/upload/book/NHN신입개발자되는법", "NHN신입개발자되는법", 100000, 2)
        );

        List<PackageItemDto> packageItemDtoList = List.of(
                new PackageItemDto(1, "싸구려 포장지", 100),
                new PackageItemDto(2, "고오급 포장지", 2000)
        );

        List<ClientAddressDto> clientAddressDtoList = List.of(
                new ClientAddressDto("우리집", "전라남도 순천시 삼산로 92-50", "대주피오레아파트 000동 000호", "1234"),
                new ClientAddressDto("조선대", "광주광역시 동구 필문대로 309", "IT융합대학 별관 101호", "4321")
        );

        List<PhoneNumberDto> phoneNumberDtoList = List.of(
                new PhoneNumberDto("내폰", "010-1234-1234"),
                new PhoneNumberDto("업무폰", "010-4321-4321")
        );

        // TODO 지우기
        shippingFee = 25000;
        minPurchasePrice = 50000;
        shippingPolicyName = "50000원 이상 구매시 무료배송";

        ClientViewOrderPostResponseDto clientViewOrderPostResponseDto = ClientViewOrderPostResponseDto.builder()
                .productItemDtoList(productItemDtoList)
                .packageItemDtoList(packageItemDtoList)
                .shippingFee(shippingFee)
                .minPurchasePrice(minPurchasePrice)
                .shippingPolicyName(shippingPolicyName)
                .clientAddressDtoList(clientAddressDtoList)
                .phoneNumberDtoList(phoneNumberDtoList)
                .build();

        return clientViewOrderPostResponseDto;
    }

    @Override
    public Page<ClientAllOrderGetResponseDto> getAllOrder(long clientId, Pageable pageable) {

        Page<Order> orderPage = orderRepository.findByClientId(clientId, pageable);

        List<ClientAllOrderGetResponseDto> clientAllOrderGetResponseDtoList = new ArrayList<>();

        orderPage.forEach( order -> {

            LocalDateTime orderDate = order.getOrderDate();
            String address = order.getDeliveryAddress();
            long totalPrice = order.getTotalPrice();
            Long pointUsageAmount = 1L; // TODO 결제 서비스에서 가져와야함.
            String couponPolicyDescription = "??"; // TODO 쿠폰 서비스에서 가져오기
            Long couponDiscountAmount = 1L; // TODO 쿠폰 서비스에서 가져오기
            long shippingFee = order.getShippingFee();
            Long totalPayAmount = 1L; // TODO 결제 서비스에서 가져오기
            List<OrderedProductDto> orderedProductDtoList = new ArrayList<>();

            orderDetailRepository.findByOrder(order).forEach(orderDetail -> {
                OrderedProductDto orderedProductDto = OrderedProductDto.builder()
                        .productId(orderDetail.getProductId())
                        .productName("!!") // TODO product-service에서 가져오기
                        .productImagePath("") // TODO product-service에서 가져오기
                        .productPrice(orderDetail.getProductPrice())
                        .quantity(orderDetail.getQuantity())
                        .orderStatus(order.getOrderStatus())
                        .packageId(1L) // TODO product-service에서 가져오기
                        .packageName("") // TODO product-service에서 가져오기
                        .packagePrice(1L) // TODO product-service에서 가져오기
                        .build();
                orderedProductDtoList.add(orderedProductDto);
            });

            clientAllOrderGetResponseDtoList.add(ClientAllOrderGetResponseDto.builder()
                    .orderDate(orderDate)
                    .address(address)
                    .orderedProductDtoList(orderedProductDtoList)
                    .totalPrice(totalPrice)
                    .pointUsageAmount(pointUsageAmount)
                    .couponPolicyDescription(couponPolicyDescription)
                    .couponDiscountAmount(couponDiscountAmount)
                    .shippingFee(shippingFee)
                    .totalPayAmount(totalPayAmount)
                    .build()
            );

        });

        // TODO 주석풀기
        //return new PageImpl<>(clientAllOrderGetResponseDtoList);


        // 아래는 임의로 만든 데이터들 입니다.

        LocalDateTime orderDate = LocalDateTime.now();
        String address = "광주광역시 동구 필문대로 309";
        OrderedProductDto orderedProductDto1 = OrderedProductDto.builder()
                .productId(1)
                .productName("이것이자바다!")
                .productImagePath("/upload/book/이것이자바다.jpg")
                .productPrice(10000)
                .quantity(1)
                .orderStatus(OrderStatus.WAIT_PAYMENT)
                .packageId(null)
                .packageName(null)
                .packagePrice(null)
                .build();
        OrderedProductDto orderedProductDto2 = OrderedProductDto.builder()
                .productId(1)
                .productName("이것이자바다!")
                .productImagePath("/upload/book/이것이자바다.jpg")
                .productPrice(10000)
                .quantity(1)
                .orderStatus(OrderStatus.WAIT_PAYMENT)
                .packageId(2L)
                .packageName("고오급 포장지")
                .packagePrice(2000L)
                .build();
        List<OrderedProductDto> orderedProductDtoList = List.of(orderedProductDto1, orderedProductDto2);

        ClientAllOrderGetResponseDto tmpRes = ClientAllOrderGetResponseDto.builder()
                .orderDate(orderDate)
                .address(address)
                .orderedProductDtoList(orderedProductDtoList)
                .totalPrice(30000)
                .pointUsageAmount(3000L)
                .couponPolicyDescription("1000원 할인 쿠폰")
                .couponDiscountAmount(1000L)
                .shippingFee(3000)
                .totalPayAmount(29000L)
                .build();


        return new PageImpl<>(List.of(tmpRes));
    }

    @Override
    public ClientOrderPostResponseDto createOrder(ClientOrderPostRequestDto clientOrderPostRequestDto, HttpServletResponse httpServletResponse) {

        // order 생성 및 저장
        Order order = Order.builder()
                .deliveryDate(clientOrderPostRequestDto.deliveryDate())
                .totalPrice(clientOrderPostRequestDto.totalPrice())
                .clientId(clientOrderPostRequestDto.clientId())
                .shippingFee(clientOrderPostRequestDto.shippingFee())
                .phoneNumber(clientOrderPostRequestDto.phoneNumber())
                .deliveryAddress(clientOrderPostRequestDto.deliveryAddress())
                .build();
        Order savedOrder = orderRepository.save(order);

        // OrderDetail 생성 및 저장
        clientOrderPostRequestDto.orderDetailDtoList().forEach(orderDetailDto -> {
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(savedOrder)
                    .quantity(orderDetailDto.quantity())
                    .productPrice(orderDetailDto.price())
                    .productId(orderDetailDto.productId())
                    .build();
            orderDetailRepository.save(orderDetail);
        });

        ClientOrderPostResponseDto responseDto = ClientOrderPostResponseDto.builder()
                .orderId(savedOrder.getId())
                .totalProductPrice(savedOrder.getTotalPrice())
                .build();

        return responseDto;
    }

}
