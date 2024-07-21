package com.nhnacademy.orderpaymentrefund.order.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class NonClientOrderGetResponseDtoTest {
    @Test
    public void testNonClientOrderGetResponseDtoBuilder() {
        // Given
        Long orderId = 1L;
        String tossOrderId = "toss-1234";
        String orderDatetime = "2024-07-21T12:34:56";
        String orderStatus = "COMPLETED";
        Long productTotalAmount = 20000L;
        Integer shippingFee = 3000;
        Long orderTotalAmount = 23000L;
        String designatedDeliveryDate = "2024-07-25";
        String deliveryStartDate = "2024-07-22";
        String phoneNumber = "01012345678";
        String deliveryAddress = "Seoul, Korea";
        String nonClientOrderPassword = "password";
        String nonClientOrdererName = "John Doe";
        String nonClientOrdererEmail = "john@example.com";

        NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem item1 =
            NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem.builder()
                .productId(1L)
                .productName("Product1")
                .productQuantity(2L)
                .productSinglePrice(10000L)
                .optionProductId(1L)
                .optionProductName("Option1")
                .optionProductQuantity(1L)
                .optionProductSinglePrice(5000L)
                .build();

        NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem item2 =
            NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem.builder()
                .productId(2L)
                .productName("Product2")
                .productQuantity(1L)
                .productSinglePrice(5000L)
                .optionProductId(2L)
                .optionProductName("Option2")
                .optionProductQuantity(1L)
                .optionProductSinglePrice(2000L)
                .build();

        List<NonClientProductOrderDetailListItem> itemList = Arrays.asList(item1, item2);

        // When
        NonClientOrderGetResponseDto dto = NonClientOrderGetResponseDto.builder()
            .orderId(orderId)
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
            .nonClientOrderPassword(nonClientOrderPassword)
            .nonClientOrdererName(nonClientOrdererName)
            .nonClientOrdererEmail(nonClientOrdererEmail)
            .build();
        itemList.forEach(dto::addNonClientProductOrderDetailList);

        // Then
        assertThat(dto.getOrderId()).isEqualTo(orderId);
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
        assertThat(dto.getNonClientOrderPassword()).isEqualTo(nonClientOrderPassword);
        assertThat(dto.getNonClientOrdererName()).isEqualTo(nonClientOrdererName);
        assertThat(dto.getNonClientOrdererEmail()).isEqualTo(nonClientOrdererEmail);
        assertThat(dto.getNonClientProductOrderDetailList()).hasSize(2);
        assertThat(dto.getNonClientProductOrderDetailList()).containsExactly(item1, item2);
    }

    @Test
    public void testNonClientOrderGetResponseDtoNoArgsConstructor() {
        // When
        NonClientOrderGetResponseDto dto = new NonClientOrderGetResponseDto();

        // Then
        assertThat(dto.getOrderId()).isNull();
        assertThat(dto.getTossOrderId()).isNull();
        assertThat(dto.getOrderDatetime()).isNull();
        assertThat(dto.getOrderStatus()).isNull();
        assertThat(dto.getProductTotalAmount()).isNull();
        assertThat(dto.getShippingFee()).isNull();
        assertThat(dto.getOrderTotalAmount()).isNull();
        assertThat(dto.getDesignatedDeliveryDate()).isNull();
        assertThat(dto.getDeliveryStartDate()).isNull();
        assertThat(dto.getPhoneNumber()).isNull();
        assertThat(dto.getDeliveryAddress()).isNull();
        assertThat(dto.getNonClientOrderPassword()).isNull();
        assertThat(dto.getNonClientOrdererName()).isNull();
        assertThat(dto.getNonClientOrdererEmail()).isNull();
        assertThat(dto.getNonClientProductOrderDetailList()).isNull();
    }

    @Test
    public void testNonClientOrderGetResponseDtoAllArgsConstructor() {
        // Given
        Long orderId = 1L;
        String tossOrderId = "toss-1234";
        String orderDatetime = "2024-07-21T12:34:56";
        String orderStatus = "COMPLETED";
        Long productTotalAmount = 20000L;
        Integer shippingFee = 3000;
        Long orderTotalAmount = 23000L;
        String designatedDeliveryDate = "2024-07-25";
        String deliveryStartDate = "2024-07-22";
        String phoneNumber = "01012345678";
        String deliveryAddress = "Seoul, Korea";
        String nonClientOrderPassword = "password";
        String nonClientOrdererName = "John Doe";
        String nonClientOrdererEmail = "john@example.com";

        // When
        NonClientOrderGetResponseDto dto = new NonClientOrderGetResponseDto(
            orderId, tossOrderId, orderDatetime, orderStatus, productTotalAmount, shippingFee, orderTotalAmount,
            designatedDeliveryDate, deliveryStartDate, phoneNumber, deliveryAddress, nonClientOrderPassword,
            nonClientOrdererName, nonClientOrdererEmail
        );

        // Then
        assertThat(dto.getOrderId()).isEqualTo(orderId);
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
        assertThat(dto.getNonClientOrderPassword()).isEqualTo(nonClientOrderPassword);
        assertThat(dto.getNonClientOrdererName()).isEqualTo(nonClientOrdererName);
        assertThat(dto.getNonClientOrdererEmail()).isEqualTo(nonClientOrdererEmail);
    }

    @Test
    public void testNonClientProductOrderDetailListItemBuilder() {
        // Given
        Long productId = 1L;
        String productName = "Product Name";
        Long productQuantity = 2L;
        Long productSinglePrice = 10000L;
        Long optionProductId = 1L;
        String optionProductName = "Option Name";
        Long optionProductQuantity = 1L;
        Long optionProductSinglePrice = 5000L;

        // When
        NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem item =
            NonClientOrderGetResponseDto.NonClientProductOrderDetailListItem.builder()
                .productId(productId)
                .productName(productName)
                .productQuantity(productQuantity)
                .productSinglePrice(productSinglePrice)
                .optionProductId(optionProductId)
                .optionProductName(optionProductName)
                .optionProductQuantity(optionProductQuantity)
                .optionProductSinglePrice(optionProductSinglePrice)
                .build();

        // Then
        assertThat(item.getProductId()).isEqualTo(productId);
        assertThat(item.getProductName()).isEqualTo(productName);
        assertThat(item.getProductQuantity()).isEqualTo(productQuantity);
        assertThat(item.getProductSinglePrice()).isEqualTo(productSinglePrice);
        assertThat(item.getOptionProductId()).isEqualTo(optionProductId);
        assertThat(item.getOptionProductName()).isEqualTo(optionProductName);
        assertThat(item.getOptionProductQuantity()).isEqualTo(optionProductQuantity);
        assertThat(item.getOptionProductSinglePrice()).isEqualTo(optionProductSinglePrice);
    }
}