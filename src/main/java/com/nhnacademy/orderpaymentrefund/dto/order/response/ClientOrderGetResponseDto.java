package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ClientOrderGetResponseDto {
    private Long orderId;
    private Long clientId;
    private Long couponId;
    private String tossOrderId;
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
    private List<ClientProductOrderDetailListItem> clientProductOrderDetailList;

    public void addClientProductOrderDetailListItem(ClientProductOrderDetailListItem item) {
        if (clientProductOrderDetailList == null) {
            clientProductOrderDetailList = new ArrayList<>();
        }
        clientProductOrderDetailList.add(item);
    }

    @Builder
    public ClientOrderGetResponseDto(Long orderId, Long clientId, Long couponId, String tossOrderId, String orderDatetime, String orderStatus, Long productTotalAmount,
                                          Integer shippingFee, Long orderTotalAmount, String designatedDeliveryDate, String deliveryStartDate, String phoneNumber,
                                          String deliveryAddress, Long discountAmountByCoupon, Long discountAmountByPoint, Long accumulatedPoint){
        this.orderId = orderId;
        this.clientId = clientId;
        this.couponId = couponId;
        this.tossOrderId = tossOrderId;
        this.orderDatetime = orderDatetime;
        this.orderStatus = orderStatus;
        this.productTotalAmount = productTotalAmount;
        this.shippingFee = shippingFee;
        this.orderTotalAmount = orderTotalAmount;
        this.designatedDeliveryDate = designatedDeliveryDate;
        this.deliveryStartDate = deliveryStartDate;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.discountAmountByCoupon = discountAmountByCoupon;
        this.discountAmountByPoint = discountAmountByPoint;
        this.accumulatedPoint = accumulatedPoint;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ClientProductOrderDetailListItem {
        private Long productOrderDetailId;

        private Long productId;
        private String productName;
        private Long productQuantity;
        private Long productSinglePrice;

        private Long optionProductId;
        private String optionProductName;
        private Long optionProductQuantity;
        private Long optionProductPrice;
    }

}
