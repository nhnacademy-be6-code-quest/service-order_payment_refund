package com.nhnacademy.orderpaymentrefund.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.orderpaymentrefund.converter.impl.ProductOrderDetailConverter;
import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.dto.order.request.OrderDetailDtoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

class ProductOrderDetailConverterTest {
    @InjectMocks
    private ProductOrderDetailConverter converter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDtoToEntity() {
        // Given
        OrderDetailDtoItem dto = new OrderDetailDtoItem();
        ReflectionTestUtils.setField(dto, "productId", 1L);
        ReflectionTestUtils.setField(dto, "quantity", 2L);
        ReflectionTestUtils.setField(dto, "productSinglePrice", 1000L);
        ReflectionTestUtils.setField(dto, "productName", "상품");

        Order order = Order.builder()
            .orderCode("uuid-1234")
            .productTotalAmount(1000L)
            .shippingFee(500)
            .designatedDeliveryDate(null)
            .phoneNumber("01012345678")
            .deliveryAddress("주소입니다")
            .build();

        ProductOrderDetail entity = converter.dtoToEntity(dto, order);

        assertThat(entity).isNotNull();
        assertThat(entity.getOrder()).isEqualTo(order);
        assertThat(entity.getProductId()).isEqualTo(1L);
        assertThat(entity.getQuantity()).isEqualTo(2);
        assertThat(entity.getPricePerProduct()).isEqualTo(1000);
        assertThat(entity.getProductName()).isEqualTo("상품");
    }
}
