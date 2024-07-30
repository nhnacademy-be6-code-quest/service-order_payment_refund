package com.nhnacademy.orderpaymentrefund.domain.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PaymentTest {

    @Test
    void testPaymentCreationSuccess() {
        // Given
        Order order = Mockito.mock(Order.class);
        long payAmount = 1000L;
        String paymentMethodName = "CreditCard";
        String tossPaymentKey = "toss-key";

        // When
        Payment payment = Payment.builder()
            .order(order)
            .payAmount(payAmount)
            .paymentMethodName(paymentMethodName)
            .paymentKey(tossPaymentKey)
            .build();

        // Then
        assertNotNull(payment);
        assertEquals(order, payment.getOrder());
        assertEquals(payAmount, payment.getPayAmount());
        assertNotNull(payment.getPayTime());
        assertEquals(paymentMethodName, payment.getPaymentMethodName());
        assertEquals(tossPaymentKey, payment.getPaymentKey());
    }



}