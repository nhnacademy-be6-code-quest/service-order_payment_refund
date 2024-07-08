package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import com.nhnacademy.orderpaymentrefund.dto.order.response.FindNonClientOrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order as o where o.orderId = :orderId and o.nonClientOrdererName = :ordererName and o.phoneNumber = :phoneNumber and o.nonClientOrdererEmail = :email")
    Optional<Order> findNonClientOrderPassword(@Param("orderId") long orderId, @Param("ordererName") String ordererName, @Param("phoneNumber") String phoneNumber, @Param("email") String email);

    // 비회원 주문 아이디 찾기
    @Query("SELECT o FROM Order o WHERE o.nonClientOrdererName = :#{#req.ordererName} " +
            "AND o.phoneNumber = :#{#req.phoneNumber} " +
            "AND o.nonClientOrdererEmail = :#{#req.email}")
    Page<Order> findNonClientOrderIdList(@Param("req") FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, @Param("pageable") Pageable pageable);

    Optional<Order> findByOrderIdAndNonClientOrderPassword(Long orderId, String ordererPassword);

    Page<Order> findByClientId(long clientId, Pageable pageable);

}
