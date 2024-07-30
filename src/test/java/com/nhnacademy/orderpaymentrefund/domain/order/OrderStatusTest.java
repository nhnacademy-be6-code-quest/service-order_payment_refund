package com.nhnacademy.orderpaymentrefund.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OrderStatusTest {
    @Test
    void testOrderStatusValues() {
        assertEquals("결제대기", OrderStatus.WAIT_PAYMENT.kor);
        assertEquals(0, OrderStatus.WAIT_PAYMENT.typeNum);

        assertEquals("결제완료", OrderStatus.PAYED.kor);
        assertEquals(1, OrderStatus.PAYED.typeNum);

        assertEquals("배송중", OrderStatus.DELIVERING.kor);
        assertEquals(2, OrderStatus.DELIVERING.typeNum);

        assertEquals("배송완료", OrderStatus.DELIVERY_COMPLETE.kor);
        assertEquals(3, OrderStatus.DELIVERY_COMPLETE.typeNum);

        assertEquals("반품", OrderStatus.REFUND.kor);
        assertEquals(4, OrderStatus.REFUND.typeNum);

        assertEquals("주문취소", OrderStatus.CANCEL.kor);
        assertEquals(5, OrderStatus.CANCEL.typeNum);

        assertEquals("반품요청", OrderStatus.REFUND_REQUEST.kor);
        assertEquals(6, OrderStatus.REFUND_REQUEST.typeNum);
    }

    @Test
    void testOrderStatusOf() {
        assertEquals(OrderStatus.WAIT_PAYMENT, OrderStatus.of("결제대기"));
        assertEquals(OrderStatus.PAYED, OrderStatus.of("결제완료"));
        assertEquals(OrderStatus.DELIVERING, OrderStatus.of("배송중"));
        assertEquals(OrderStatus.DELIVERY_COMPLETE, OrderStatus.of("배송완료"));
        assertEquals(OrderStatus.REFUND, OrderStatus.of("반품"));
        assertEquals(OrderStatus.CANCEL, OrderStatus.of("주문취소"));
        assertEquals(OrderStatus.REFUND_REQUEST, OrderStatus.of("반품요청"));
    }

    @Test
    void testOrderStatusOfWithQuotes() {
        assertEquals(OrderStatus.WAIT_PAYMENT, OrderStatus.of("'결제대기'"));
        assertEquals(OrderStatus.PAYED, OrderStatus.of("\"결제완료\""));
    }

    @Test
    void testOrderStatusOfInvalidValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderStatus.of("잘못된값");
        });

        String expectedMessage = "OrderStatus의 value값이 잘못 되었습니다. '결제대기', '결제완료', '배송중', '반품', '배송완료', '반품요청' 중 하나를 기입하세요";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testToString() {
        assertEquals("결제대기", OrderStatus.WAIT_PAYMENT.toString());
        assertEquals("결제완료", OrderStatus.PAYED.toString());
        assertEquals("배송중", OrderStatus.DELIVERING.toString());
        assertEquals("배송완료", OrderStatus.DELIVERY_COMPLETE.toString());
        assertEquals("반품", OrderStatus.REFUND.toString());
        assertEquals("주문취소", OrderStatus.CANCEL.toString());
        assertEquals("반품요청", OrderStatus.REFUND_REQUEST.toString());
    }

}
