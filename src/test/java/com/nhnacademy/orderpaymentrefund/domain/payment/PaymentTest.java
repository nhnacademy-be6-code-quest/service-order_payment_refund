//package com.nhnacademy.orderpaymentrefund.domain.payment;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.within;
//
//import com.nhnacademy.orderpaymentrefund.domain.order.Order;
//import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//@DataJpaTest
//@ActiveProfiles("test")
//class PaymentTest {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private Order testOrder;
//
//    @BeforeEach
//    void setUp() {
//        testOrder = createOrderInstance();
//        entityManager.persist(testOrder);
//    }
//
//    @Test
//    void testPaymentCreation() {
//        Payment payment = Payment.builder()
//            .order(testOrder)
//            .payAmount(1000L)
//            .paymentMethodName("Credit Card")
//            .tossPaymentKey("unique-key")
//            .build();
//
//        entityManager.persist(payment);
//        entityManager.flush(); // 데이터베이스에 커밋
//
//        Payment savedPayment = entityManager.find(Payment.class, payment.getPaymentId());
//
//        assertThat(savedPayment).isNotNull();
//        assertThat(savedPayment.getPaymentId()).isGreaterThan(0);
//        assertThat(savedPayment.getOrder()).isEqualTo(testOrder);
//        assertThat(savedPayment.getPayAmount()).isEqualTo(1000L);
//        assertThat(savedPayment.getPaymentMethodName()).isEqualTo("Credit Card");
//        assertThat(savedPayment.getTossPaymentKey()).isEqualTo("unique-key");
//        // payTime 검증
//        assertThat(savedPayment.getPayTime()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
//    }
//
//    private Order createOrderInstance() {
//        return Order.clientOrderBuilder()
//            .clientId(1L)
//            .tossOrderId("toss-order-id")
//            .productTotalAmount(5000L)
//            .shippingFee(500)
//            .designatedDeliveryDate(LocalDate.now()) // LocalDateTime으로 변경
//            .phoneNumber("01012345678")
//            .deliveryAddress("서울시 강남구 역삼동 123-45")
//            .discountAmountByCoupon(0L)
//            .discountAmountByPoint(0L)
//            .accumulatedPoint(0L)
//            .build();
//    }
//
//}
