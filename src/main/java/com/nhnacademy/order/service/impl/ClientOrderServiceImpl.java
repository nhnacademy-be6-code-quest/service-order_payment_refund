package com.nhnacademy.order.service.impl;

import com.nhnacademy.order.domain.order.OrderStatus;
import com.nhnacademy.order.dto.order.request.client.ClientOrderPostRequestDto;
import com.nhnacademy.order.dto.order.response.client.ClientAllOrderGetResponseDto;
import com.nhnacademy.order.dto.order.response.client.ClientOrderPostResponseDto;
import com.nhnacademy.order.dto.order.response.field.OrderedProductDto;
import com.nhnacademy.order.dto.order.response.field.PackageItemDto;
import com.nhnacademy.order.dto.order.response.field.ProductItemDto;
import com.nhnacademy.order.repository.OrderRepository;
import com.nhnacademy.order.service.ClientOrderService;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ClientOrderServiceImpl implements ClientOrderService {

    private OrderRepository orderRepository;

    @Override
    // TODO 각 서비스의 api 문서가 나와야 완성 가능. 현재, 해당 프로젝트에서 끌어올 수 없는 데이터는 그냥 임의로 생성!
    // TODO 임의로 생성한 데이터 클래스에서 AllArgs 애노테이션 삭제하기
    public ClientOrderPostResponseDto tryOrder(long clientId, ClientOrderPostRequestDto clientOrderPostRequestDto) {

        List<ProductItemDto> productItemDtoList = List.of(
                new ProductItemDto(1, "/upload/book/이것이자바다.jpg", "이것이자바다!", 10000, 1),
                new ProductItemDto(2, "/upload/book/이것이자바니.jpg", "이것이자바니?", 15000, 3),
                new ProductItemDto(3, "/upload/book/NHN신입개발자되는법", "NHN신입개발자되는법", 100000, 2)
        );
        List<PackageItemDto> packageItemDtoList = List.of(
                new PackageItemDto(1, "싸구려 포장지", 100),
                new PackageItemDto(2, "고오급 포장지", 2000)
        );
        long shippingFee = 25000;
        long minPurchasePrice = 50000;
        String shippingPolicyName = "50000원 이상 구매시 무료배송";

        ClientOrderPostResponseDto tmpRes = new ClientOrderPostResponseDto(
                productItemDtoList,
                packageItemDtoList,
                shippingFee,
                minPurchasePrice,
                shippingPolicyName
        );

        return tmpRes;
    }

    @Override
    public List<ClientAllOrderGetResponseDto> getAllOrder(long clientId) {

        ZonedDateTime orderDate = ZonedDateTime.now();
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
                .totalProductPrice(30000)
                .pointUsageAmount(3000)
                .couponPolicyDescription("1000원 할인 쿠폰")
                .couponDiscountAmount(1000)
                .shippingFee(3000)
                .totalPayAmount(29000)
                .build();


        return List.of(tmpRes);
    }

}
