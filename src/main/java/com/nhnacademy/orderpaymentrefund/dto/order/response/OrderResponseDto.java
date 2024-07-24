package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OrderResponseDto {

    private Long orderId;
    private Long clientId;
    private Long couponId;
    private String orderCode;
    private String orderDatetime;
    private String orderStatus;
    private Long productTotalAmount;
    private Integer shippingFee;
    private Long orderTotalAmount;
    private String designatedDeliveryDate;
    private String deliveryStartDate;
    private String phoneNumber;
    private String deliveryAddress;
    private Long discountAmountByCoupon;
    private Long discountAmountByPoint;
    private Long accumulatedPoint;
    private String nonClientOrderPassword;
    private String nonClientOrdererName;
    private String nonClientOrdererEmail;
    private List<OrderListItem> orderListItemList;

    public void addClientOrderListItem(OrderListItem item) {
        if(orderListItemList == null){
            orderListItemList = new ArrayList<>();
        }
        orderListItemList.add(item);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class OrderListItem{
        private Long productOrderDetailId;

        private Long productId;
        private String productName;
        private Long productQuantity;
        private Long productSinglePrice;

        private Long optionProductId;
        private String optionProductName;
        private Long optionProductQuantity;
        private Long optionProductSinglePrice;
    }
}
