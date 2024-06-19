package com.nhnacademy.payment.service;

import com.nhnacademy.payment.domain.Payment;
import com.nhnacademy.payment.dto.PaymentCreateRequestGet;
import com.nhnacademy.payment.dto.PaymentResponse;

/**
 * 결제의 CRUD 작업을 수행하는 비즈니스 로직을 구현했습니다.
 * C : savePayment 메서드
 * R : findPaymentByOrderId 메서드 - 결제 정보를 read 하는 경우는 주문에서 결제 관련 정보가 필요한 경우이기에 param 을 orderId 로 뒀습니다.
 * U : 결제는 사라지지 않습니다. 환불 또는 취소 시에는 주문 테이블에서 주문 상태 속성이 변경됩니다.
 * D : 결제는 사라지지 않습니다.
 *
 * @author Virtus_Chae
 * @version 1.0
 */
public interface PaymentService {

    /**
     * 주문 아이디를 받아서 결제 정보를 반환합니다. 사용자가 주문 내역을 조회할 때 사용됩니다. (Order 쪽에서 사용)
     *
     * @param orderId 주문 아이디
     * @return 결제 정보
     */
    PaymentResponse findPaymentByOrderId(Long orderId);

    /**
     * 결제 아이디를 받아서 결제 정보를 반환합니다. 환불을 확정할 때 사용합니다.
     *
     * @param paymentId 주문 아이디
     * @return 결제 정보
     */
    PaymentResponse findPaymentByPaymentId(Long paymentId);

    /**
     * 결제 정보를 저장합니다. 결제를 생성할 때 사용됩니다.
     *
     * @param payment 저장할 결제 정보
     * @return 저장된 결제 정보
     */
    Payment savePayment(Payment payment);

    /**
     * 결제와 관련된 정보를 불러 와 사용자에게 불러 옵니다. 사용자가 가지고 있는 잔여 포인트, 적용할 쿠폰을 View 에 보여주는 데에 사용됩니다.
     *
     * @param paymentCreateRequestGet 결제할 때 사용자에게 보여줘야 할 정보 : 사용자의 포인트, 쿠폰 리스트
     */
    void tryPayment(PaymentCreateRequestGet paymentCreateRequestGet);
}