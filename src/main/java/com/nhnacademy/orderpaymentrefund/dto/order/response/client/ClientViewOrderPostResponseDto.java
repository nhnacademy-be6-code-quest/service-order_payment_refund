package com.nhnacademy.orderpaymentrefund.dto.order.response.client;

import com.nhnacademy.orderpaymentrefund.dto.order.response.field.ClientAddressDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.field.PackageItemDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.field.PhoneNumberDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.field.ProductItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientViewOrderPostResponseDto { // 주문 페이지 화면에 뿌려질 데이터

    // 상품관련
    private List<ProductItemDto> productItemDtoList; // 사용자가 주문하려는 상품들
    private List<PackageItemDto> packageItemDtoList; // 데이터베이스에 등록된 '포장지' 목록

    // 배송비관련
    private int shippingFee; // 배송비
    private int minPurchasePrice; // 무료배송 최소 금액
    private String shippingPolicyName; // 배송비 정책 이름

    // 회원관련
    private List<ClientAddressDto> clientAddressDtoList; // 데이터베이스에 등록된 회원 주소
    private List<PhoneNumberDto> phoneNumberDtoList; // 데이터베이스에 등록된 전화번호

}
