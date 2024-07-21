package com.nhnacademy.orderpaymentrefund.order.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.ClientOrderGetResponseDto.ClientProductOrderDetailListItem;
import java.util.List;
import org.junit.jupiter.api.Test;

class ClientOrderGetResponseDtoTest {
    @Test
    public void testClientOrderGetResponseDtoBuilder() {
        // Given
        Long orderId = 1L;
        Long clientId = 2L;
        Long couponId = 3L;
        String tossOrderId = "order-1234";
        String orderDatetime = "2024-07-21T12:34:56";
        String orderStatus = "COMPLETED";
        Long productTotalAmount = 20000L;
        Integer shippingFee = 3000;
        Long orderTotalAmount = 23000L;
        String designatedDeliveryDate = "2024-08-10";
        String deliveryStartDate = "2024-08-05";
        String phoneNumber = "010-1234-5678";
        String deliveryAddress = "123 Some Street";
        Long discountAmountByCoupon = 2000L;
        Long discountAmountByPoint = 1000L;
        Long accumulatedPoint = 500L;

        // When
        ClientOrderGetResponseDto dto = ClientOrderGetResponseDto.builder()
            .orderId(orderId)
            .clientId(clientId)
            .couponId(couponId)
            .tossOrderId(tossOrderId)
            .orderDatetime(orderDatetime)
            .orderStatus(orderStatus)
            .productTotalAmount(productTotalAmount)
            .shippingFee(shippingFee)
            .orderTotalAmount(orderTotalAmount)
            .designatedDeliveryDate(designatedDeliveryDate)
            .deliveryStartDate(deliveryStartDate)
            .phoneNumber(phoneNumber)
            .deliveryAddress(deliveryAddress)
            .discountAmountByCoupon(discountAmountByCoupon)
            .discountAmountByPoint(discountAmountByPoint)
            .accumulatedPoint(accumulatedPoint)
            .build();

        // Then
        assertThat(dto.getOrderId()).isEqualTo(orderId);
        assertThat(dto.getClientId()).isEqualTo(clientId);
        assertThat(dto.getCouponId()).isEqualTo(couponId);
        assertThat(dto.getTossOrderId()).isEqualTo(tossOrderId);
        assertThat(dto.getOrderDatetime()).isEqualTo(orderDatetime);
        assertThat(dto.getOrderStatus()).isEqualTo(orderStatus);
        assertThat(dto.getProductTotalAmount()).isEqualTo(productTotalAmount);
        assertThat(dto.getShippingFee()).isEqualTo(shippingFee);
        assertThat(dto.getOrderTotalAmount()).isEqualTo(orderTotalAmount);
        assertThat(dto.getDesignatedDeliveryDate()).isEqualTo(designatedDeliveryDate);
        assertThat(dto.getDeliveryStartDate()).isEqualTo(deliveryStartDate);
        assertThat(dto.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(dto.getDeliveryAddress()).isEqualTo(deliveryAddress);
        assertThat(dto.getDiscountAmountByCoupon()).isEqualTo(discountAmountByCoupon);
        assertThat(dto.getDiscountAmountByPoint()).isEqualTo(discountAmountByPoint);
        assertThat(dto.getAccumulatedPoint()).isEqualTo(accumulatedPoint);
    }

    @Test
    public void testAddClientProductOrderDetailListItem() {
        // Given
        ClientOrderGetResponseDto.ClientProductOrderDetailListItem item1 = ClientOrderGetResponseDto.ClientProductOrderDetailListItem.builder()
            .productOrderDetailId(1L)
            .productId(101L)
            .productName("Product A")
            .productQuantity(2L)
            .productSinglePrice(5000L)
            .build();

        ClientOrderGetResponseDto.ClientProductOrderDetailListItem item2 = ClientOrderGetResponseDto.ClientProductOrderDetailListItem.builder()
            .productOrderDetailId(2L)
            .productId(102L)
            .productName("Product B")
            .productQuantity(1L)
            .productSinglePrice(10000L)
            .build();

        ClientOrderGetResponseDto dto = ClientOrderGetResponseDto.builder().build();

        // When
        dto.addClientProductOrderDetailListItem(item1);
        dto.addClientProductOrderDetailListItem(item2);

        // Then
        List<ClientProductOrderDetailListItem> items = dto.getClientProductOrderDetailList();
        assertThat(items).isNotNull();
        assertThat(items).hasSize(2);
        assertThat(items).containsExactly(item1, item2);
    }
}
