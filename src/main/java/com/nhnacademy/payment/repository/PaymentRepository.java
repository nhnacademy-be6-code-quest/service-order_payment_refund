package com.nhnacademy.payment.repository;

import com.nhnacademy.payment.domain.Payment;
import java.util.Optional;

/**
 * @author Virtus_Chae
 * @version 0.0
 */
public interface PaymentRepository {

    /**
     * 결제 아이디를 받아 결제 정보를 반환하는 메서드입니다.
     * 데이터베이스에서 해당 결제 정보를 찾지 못한 경우 Optional.empty()를 반환하기 위해 Optional 타입을 사용합니다.
     *
     * @param paymentId 조회할 결제 아이디
     * @return 결제 정보. 데이터를 찾지 못한 경우 Optional.empty() 반환
     */
    Optional<Payment> findById(Long paymentId);

    /**
     * 결제 정보를 DataBase 에 저장하는 메서드입니다.
     *
     * @param payment
     * @return
     */
    Payment save(Payment payment);
}
