package com.nhnacademy.order.controller;

import com.nhnacademy.order.dto.order.request.test.OrderTryRequestDto;
import com.nhnacademy.order.dto.order.response.test.OrderTryResponseDto;
import com.nhnacademy.order.dto.order.response.field.PackageItemDto;
import com.nhnacademy.order.dto.order.response.field.ProductItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class TestOrderServiceController { // view를 위한 !!

    @PostMapping("/try") // 주문시도 - '주문하기'버튼 눌렀을 때 호출하는 api
    public ResponseEntity<OrderTryResponseDto> tryOrder(@RequestBody OrderTryRequestDto orderRequestDto) {
        List<ProductItemDto> productItemDtoList = new ArrayList<>();
        productItemDtoList.add(new ProductItemDto(1, "상품A이미지경로", "상품A", 10000, 1));
        productItemDtoList.add(new ProductItemDto(2, "상품B이미지경로", "상품B", 20000, 2));
        List<PackageItemDto> packageItemDtoList = new ArrayList<>();
        packageItemDtoList.add(new PackageItemDto(1, "포장지A", 1000));
        packageItemDtoList.add(new PackageItemDto(2, "포장지B", 500));
        OrderTryResponseDto responseDto = new OrderTryResponseDto(productItemDtoList, packageItemDtoList, 3000, 30000, "배송정책이르음");
        return ResponseEntity.ok(responseDto);
    }
}
