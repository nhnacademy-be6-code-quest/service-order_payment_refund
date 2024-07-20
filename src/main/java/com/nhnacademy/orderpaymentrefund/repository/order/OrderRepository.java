package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import com.nhnacademy.orderpaymentrefund.dto.order.request.FindNonClientOrderIdRequestDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.nhnacademy.orderpaymentrefund.dto.payment.response.PostProcessRequiredPaymentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId and o.nonClientOrdererName = :ordererName and o.phoneNumber = :phoneNumber and o.nonClientOrdererEmail = :email")
    Optional<Order> findNonClientOrderPassword(@Param("orderId") Long orderId, @Param("ordererName") String ordererName, @Param("phoneNumber") String phoneNumber, @Param("email") String email);

    // 비회원 주문 아이디 찾기
    @Query("SELECT o FROM Order o WHERE o.nonClientOrdererName = :#{#req.ordererName} " +
        "AND o.phoneNumber = :#{#req.phoneNumber} " +
        "AND o.nonClientOrdererEmail = :#{#req.email}")
    Page<Order> findNonClientOrderIdList(@Param("req") FindNonClientOrderIdRequestDto findNonClientOrderIdRequestDto, @Param("pageable") Pageable pageable);

    Optional<Order> findByOrderIdAndNonClientOrderPassword(Long orderId, String ordererPassword);

    Page<Order> findByClientId(long clientId, Pageable pageable);

    @Query("SELECT SUM(o.productTotalAmount - o.discountAmountByCoupon - o.discountAmountByPoint) " +
        "FROM Order o " +
        "WHERE (o.orderStatus = 1 OR o.orderStatus = 2 OR o.orderStatus = 3) " +
        "  AND o.orderDatetime >= :startDate " +
        "  AND o.clientId = :clientId")
    Long sumFinalAmountForCompletedOrders(Long clientId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT SUM(podo.optionProductPrice) " +
        "FROM ProductOrderDetailOption podo " +
        "LEFT JOIN ProductOrderDetail pod on pod.productOrderDetailId = podo.productOrderDetail.productOrderDetailId " +
        "LEFT JOIN Order o on o.orderId = pod.order.orderId " +
        "WHERE (o.orderStatus = 1 OR o.orderStatus = 2 OR o.orderStatus = 3) " +
        "AND o.orderDatetime >= :startDate " +
        "AND o.clientId = :clientId")
    Long getTotalOptionPriceForLastThreeMonths(Long clientId, @Param("startDate") LocalDateTime localDateTime);

    Optional<Order> getOrderByTossOrderId(String tossOrderId);

    List<Order> findAllByOrderStatusAndOrderDatetimeBefore(OrderStatus orderStatus, LocalDateTime orderDatetime);
}