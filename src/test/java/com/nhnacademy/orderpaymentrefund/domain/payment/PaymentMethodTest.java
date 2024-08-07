package com.nhnacademy.orderpaymentrefund.domain.payment;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test") // 테스트 프로파일을 사용하는 경우
class PaymentMethodTest {

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        // 초기 설정이 필요한 경우 작성
    }

    @Test
    void testPaymentMethodCreation() {
        PaymentMethodType paymentMethodType = new PaymentMethodType();
        paymentMethodType.setMethodName("Credit Card");

        PaymentMethodType savedPaymentMethodType = entityManager.persistFlushFind(paymentMethodType);

        assertThat(savedPaymentMethodType).isNotNull();
        assertThat(savedPaymentMethodType.getPaymentMethodTypeId()).isPositive();
        assertThat(savedPaymentMethodType.getPaymentMethodTypeName()).isEqualTo("Credit Card");
    }
}
