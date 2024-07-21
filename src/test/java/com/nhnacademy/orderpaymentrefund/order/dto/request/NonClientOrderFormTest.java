package com.nhnacademy.orderpaymentrefund.order.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.dto.order.request.NonClientOrderForm;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class NonClientOrderFormTest {
    @Test
    public void testNonClientOrderForm() {
        // Given
        NonClientOrderForm.OrderDetailDtoItem item1 = NonClientOrderForm.OrderDetailDtoItem.builder()
            .productId(1L)
            .productName("Product A")
            .quantity(2L)
            .productSinglePrice(10000L)
            .packableProduct(true)
            .build();

        NonClientOrderForm.OrderDetailDtoItem item2 = NonClientOrderForm.OrderDetailDtoItem.builder()
            .productId(2L)
            .productName("Product B")
            .quantity(1L)
            .productSinglePrice(20000L)
            .packableProduct(false)
            .build();

        List<NonClientOrderForm.OrderDetailDtoItem> orderDetailDtoItemList = Arrays.asList(item1, item2);

        Integer shippingFee = 3000;
        Long productTotalAmount = 40000L;
        Long payAmount = 43000L;
        String orderedPersonName = "홍길동";
        String email = "hong@example.com";
        String phoneNumber = "010-1234-5678";
        String addressZipCode = "12345";
        String deliveryAddress = "Seoul, Korea";
        String deliveryDetailAddress = "Apartment 101";
        Boolean useDesignatedDeliveryDate = true;
        LocalDate designatedDeliveryDate = LocalDate.of(2024, 8, 10);
        Integer paymentMethod = 1;
        String orderPassword = "password123";
        String tossOrderId = "toss-1234";

        // When
        NonClientOrderForm form = NonClientOrderForm.builder()
            .shippingFee(shippingFee)
            .productTotalAmount(productTotalAmount)
            .payAmount(payAmount)
            .orderedPersonName(orderedPersonName)
            .email(email)
            .phoneNumber(phoneNumber)
            .addressZipCode(addressZipCode)
            .deliveryAddress(deliveryAddress)
            .deliveryDetailAddress(deliveryDetailAddress)
            .useDesignatedDeliveryDate(useDesignatedDeliveryDate)
            .designatedDeliveryDate(designatedDeliveryDate)
            .paymentMethod(paymentMethod)
            .orderPassword(orderPassword)
            .tossOrderId(tossOrderId)
            .build();

        // Then
        assertThat(form.getOrderDetailDtoItemList()).isEqualTo(orderDetailDtoItemList);
        assertThat(form.getShippingFee()).isEqualTo(shippingFee);
        assertThat(form.getProductTotalAmount()).isEqualTo(productTotalAmount);
        assertThat(form.getPayAmount()).isEqualTo(payAmount);
        assertThat(form.getOrderedPersonName()).isEqualTo(orderedPersonName);
        assertThat(form.getEmail()).isEqualTo(email);
        assertThat(form.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(form.getAddressZipCode()).isEqualTo(addressZipCode);
        assertThat(form.getDeliveryAddress()).isEqualTo(deliveryAddress);
        assertThat(form.getDeliveryDetailAddress()).isEqualTo(deliveryDetailAddress);
        assertThat(form.getUseDesignatedDeliveryDate()).isEqualTo(useDesignatedDeliveryDate);
        assertThat(form.getDesignatedDeliveryDate()).isEqualTo(designatedDeliveryDate);
        assertThat(form.getPaymentMethod()).isEqualTo(paymentMethod);
        assertThat(form.getOrderPassword()).isEqualTo(orderPassword);
        assertThat(form.getTossOrderId()).isEqualTo(tossOrderId);
    }
}