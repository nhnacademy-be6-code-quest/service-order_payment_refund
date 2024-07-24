package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NonClientOrderRepository extends JpaRepository<NonClientOrder, Long> {

    Optional<NonClientOrder> findByOrder_OrderId(Long orderId);

    Optional<NonClientOrder> findByNonClientOrderPasswordEqualsAndOrder_OrderCodeEquals(String nonClientOrderPassword, String orderCode);

    Optional<NonClientOrder> findByNonClientOrderPasswordEqualsAndOrder_OrderId(String nonClientOrderPassword, Long orderId);

    Boolean existsByOrder_OrderId(Long orderId);

    Page<NonClientOrder> findByNonClientOrdererNameAndNonClientOrdererEmailAndOrder_PhoneNumber(String nonClientOrdererName, String nonClientOrdererEmail, String phoneNumber, Pageable pageable);

    Optional<NonClientOrder> findByNonClientOrdererNameAndNonClientOrdererEmailAndOrder_PhoneNumber(String nonClientOrdererName, String nonClientOrdererEmail, String phoneNumber);

}
