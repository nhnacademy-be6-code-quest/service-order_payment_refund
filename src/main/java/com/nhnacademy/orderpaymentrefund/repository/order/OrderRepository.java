package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order as o where o.nonClientOrdererName = :ordererName and o.phoneNumber = :phoneNumber and o.nonClientOrdererEmail = :email")
    Optional<Order> findNonClientOrderId(@Param("ordererName") String ordererName, @Param("phoneNumber") String phoneNumber, @Param("email") String email);
    @Query("select o from Order as o where o.orderId = :orderId and o.nonClientOrdererName = :ordererName and o.phoneNumber = :phoneNumber and o.nonClientOrdererEmail = :email")
    Optional<Order> findNonClientOrderPassword(@Param("orderId") long orderId, @Param("ordererName") String ordererName, @Param("phoneNumber") String phoneNumber, @Param("email") String email);
}
