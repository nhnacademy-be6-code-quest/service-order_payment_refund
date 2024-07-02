package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order as o where o.nonClientOrdererName = :ordererName and o.phoneNumber = :phoneNumber and o.nonClientOrdererEmail = :email")
    Page<Order> findNonClientOrderId(@Param("ordererName") String ordererName, @Param("phoneNumber") String phoneNumber, @Param("email") String email);
    @Query("select o from Order as o where o.orderId = :orderId and o.nonClientOrdererName = :ordererName and o.phoneNumber = :phoneNumber and o.nonClientOrdererEmail = :email")
    Optional<Order> findNonClientOrderPassword(@Param("orderId") long orderId, @Param("ordererName") String ordererName, @Param("phoneNumber") String phoneNumber, @Param("email") String email);

    Page<Order> findAllByNonClientOrdererEmailAndPhoneNumberAndNonClientOrdererName(String nonClientOrdererEmail, String phoneNumber, String clientOrdererName, Pageable pageable);
    Optional<Order> findByOrderIdAndNonClientOrderPassword(Long orderId, String ordererPassword);
    Page<Order> findByClientId(long clientId, Pageable pageable);
}
