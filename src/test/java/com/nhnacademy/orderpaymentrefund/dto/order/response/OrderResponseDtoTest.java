package com.nhnacademy.orderpaymentrefund.dto.order.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.OrderResponseDto.OrderListItem;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderResponseDtoTest {
    @Test
    void testOrderResponseDtoBuilder() {
        // Given
        OrderResponseDto.OrderListItem item1 = OrderResponseDto.OrderListItem.builder()
            .productOrderDetailId(1L)
            .productId(101L)
            .productName("Product 1")
            .productQuantity(2L)
            .productSinglePrice(1000L)
            .optionProductId(201L)
            .optionProductName("Option 1")
            .optionProductQuantity(1L)
            .optionProductSinglePrice(500L)
            .build();

        OrderResponseDto.OrderListItem item2 = OrderResponseDto.OrderListItem.builder()
            .productOrderDetailId(2L)
            .productId(102L)
            .productName("Product 2")
            .productQuantity(1L)
            .productSinglePrice(2000L)
            .optionProductId(202L)
            .optionProductName("Option 2")
            .optionProductQuantity(2L)
            .optionProductSinglePrice(1000L)
            .build();

        List<OrderListItem> itemList = List.of(item1, item2);

        // When
        OrderResponseDto dto = OrderResponseDto.builder()
            .orderId(1L)
            .clientId(1L)
            .couponId(1L)
            .orderCode("uuid-1234")
            .orderDatetime("2024-07-21 10:00:00")
            .orderStatus("Pending")
            .productTotalAmount(3000L)
            .shippingFee(3000)
            .orderTotalAmount(6000L)
            .designatedDeliveryDate("2024-08-10")
            .deliveryStartDate("2024-07-22")
            .phoneNumber("01012341234")
            .deliveryAddress("우리집주소")
            .discountAmountByCoupon(1000L)
            .discountAmountByPoint(500L)
            .accumulatedPoint(100L)
            .nonClientOrderPassword("password")
            .nonClientOrdererName("홍길동")
            .nonClientOrdererEmail("hong@example.com")
            .orderListItemList(itemList)
            .build();

        // Then
        assertThat(dto.getOrderId()).isEqualTo(1L);
        assertThat(dto.getClientId()).isEqualTo(1L);
        assertThat(dto.getCouponId()).isEqualTo(1L);
        assertThat(dto.getOrderCode()).isEqualTo("uuid-1234");
        assertThat(dto.getOrderDatetime()).isEqualTo("2024-07-21 10:00:00");
        assertThat(dto.getOrderStatus()).isEqualTo("Pending");
        assertThat(dto.getProductTotalAmount()).isEqualTo(3000L);
        assertThat(dto.getShippingFee()).isEqualTo(3000);
        assertThat(dto.getOrderTotalAmount()).isEqualTo(6000L);
        assertThat(dto.getDesignatedDeliveryDate()).isEqualTo("2024-08-10");
        assertThat(dto.getDeliveryStartDate()).isEqualTo("2024-07-22");
        assertThat(dto.getPhoneNumber()).isEqualTo("01012341234");
        assertThat(dto.getDeliveryAddress()).isEqualTo("우리집주소");
        assertThat(dto.getDiscountAmountByCoupon()).isEqualTo(1000L);
        assertThat(dto.getDiscountAmountByPoint()).isEqualTo(500L);
        assertThat(dto.getAccumulatedPoint()).isEqualTo(100L);
        assertThat(dto.getNonClientOrderPassword()).isEqualTo("password");
        assertThat(dto.getNonClientOrdererName()).isEqualTo("홍길동");
        assertThat(dto.getNonClientOrdererEmail()).isEqualTo("hong@example.com");
        assertThat(dto.getOrderListItemList()).isEqualTo(itemList);
    }

    @Test
    void testAddClientOrderListItem() {
        // Given
        OrderResponseDto dto = new OrderResponseDto();
        OrderResponseDto.OrderListItem item = OrderResponseDto.OrderListItem.builder()
            .productOrderDetailId(1L)
            .productId(101L)
            .productName("Product 1")
            .productQuantity(2L)
            .productSinglePrice(1000L)
            .optionProductId(201L)
            .optionProductName("Option 1")
            .optionProductQuantity(1L)
            .optionProductSinglePrice(500L)
            .build();

        // When
        dto.addClientOrderListItem(item);

        // Then
        assertThat(dto.getOrderListItemList()).containsExactly(item);
    }
}

