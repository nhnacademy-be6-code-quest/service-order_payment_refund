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
public class ClientOrderListGetResponseDto {

    private String orderDate;
    private Long orderId;
    private String orderStatus;
    private List<ClientOrderListItem> clientOrderListItemList;

    public void addClientOrderListItem(ClientOrderListItem clientOrderListItem) {
        if(clientOrderListItemList == null){
            clientOrderListItemList = new ArrayList<>();
        }
        clientOrderListItemList.add(clientOrderListItem);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    public static class ClientOrderListItem{
        private Long productId;
        private String productName;
        private Long productQuantity;
        private Long productSinglePrice;

        private Long optionProductId;
        private Long optionProductName;
        private Long optionProductQuantity;
        private Long optionProductSinglePrice;

    }

}
