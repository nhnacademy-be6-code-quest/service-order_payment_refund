package com.nhnacademy.orderpaymentrefund.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class NonClientOrderGetResponseDto {
    private Long orderId;
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
    private String nonClientOrderPassword;
    private String nonClientOrdererName;
    private String nonClientOrdererEmail;
    private List<NonClientProductOrderDetailListItem> nonClientProductOrderDetailList;

    public void addNonClientProductOrderDetailList(NonClientProductOrderDetailListItem item){
        if(nonClientProductOrderDetailList == null){
            nonClientProductOrderDetailList = new ArrayList<>();
        }
        nonClientProductOrderDetailList.add(item);
    }

    @Builder
    public NonClientOrderGetResponseDto(Long orderId, String tossOrderId, String orderDatetime, String orderStatus, Long productTotalAmount,
                                     Integer shippingFee, Long orderTotalAmount, String designatedDeliveryDate, String deliveryStartDate, String phoneNumber,
                                     String deliveryAddress, String nonClientOrderPassword, String nonClientOrdererName, String nonClientOrdererEmail){
        this.orderId = orderId;
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
        this.nonClientOrderPassword = nonClientOrderPassword;
        this.nonClientOrdererName = nonClientOrdererName;
        this.nonClientOrdererEmail = nonClientOrdererEmail;
    }

    @NoArgsConstructor
    @Getter
    @Builder
    @AllArgsConstructor
    public static class NonClientProductOrderDetailListItem{
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
