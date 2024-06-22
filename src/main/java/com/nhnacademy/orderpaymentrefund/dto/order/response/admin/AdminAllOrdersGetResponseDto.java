package com.nhnacademy.orderpaymentrefund.dto.order.response.admin;

import com.nhnacademy.orderpaymentrefund.dto.order.response.field.OrderedProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
// 관리자가 보는,, 회원이 주문한 주문 내역 건
public class AdminAllOrdersGetResponseDto {
    private long clientId; // 회원 아이디
    private String clientEmail; // 회원 이메일
    private ZonedDateTime orderDate; // 주문날짜
    private List<OrderedProductDto> orderedProductDtoList; // 주문한 상품정보들
}
