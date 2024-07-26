package com.nhnacademy.orderpaymentrefund.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailOptionConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ProductOrderDetailOptionConverterTest {

    @InjectMocks
    private ProductOrderDetailOptionConverter converter;

    @Test
    void testDtoToEntity() {
        // Given
        OrderDetailDtoItem dto = new OrderDetailDtoItem();
        ReflectionTestUtils.setField(dto, "productId", 1L);
        ReflectionTestUtils.setField(dto, "optionProductName", "포장지상품이름");
        ReflectionTestUtils.setField(dto, "productSinglePrice", 1000L);

        Order order = Order.builder()
            .orderCode("uuid-1234")
            .productTotalAmount(2000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("Test Address")
            .build();

        ProductOrderDetail productOrderDetail = ProductOrderDetail.builder()
            .order(order)
            .productId(1L)
            .quantity(2L)
            .pricePerProduct(1000L)
            .productName("포장지상품이름")
            .build();

        // When
        ProductOrderDetailOption entity = converter.dtoToEntity(dto, productOrderDetail);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getProductOrderDetail()).isEqualTo(productOrderDetail);
        assertThat(entity.getProductId()).isEqualTo(1L);
        assertThat(entity.getOptionProductName()).isEqualTo("포장지상품이름");
        assertThat(entity.getOptionProductPrice()).isZero();
    }
}
