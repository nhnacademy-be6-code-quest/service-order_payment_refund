package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.ClientOrder;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {

    Optional<ClientOrder> findByOrder_OrderId(Long orderId);

    @Query("SELECT SUM(o.productTotalAmount - co.discountAmountByCoupon - co.discountAmountByPoint) " +
        "FROM Order o " +
        "LEFT JOIN ClientOrder co on o.orderId = co.order.orderId " +
        "WHERE (o.orderStatus = 1 OR o.orderStatus = 2 OR o.orderStatus = 3) " +
        "  AND o.orderDatetime >= :startDate " +
        "  AND co.clientId = :clientId")
    Long sumFinalAmountForCompletedOrders(Long clientId, @Param("startDate") LocalDateTime startDate);


    @Query("SELECT SUM(podo.optionProductPrice) " +
        "FROM ClientOrder co " +
        "LEFT JOIN co.order o " +
        "LEFT JOIN o.productOrderDetailList pod " +
        "LEFT JOIN pod.productOrderDetailOptionList podo " +
        "WHERE (o.orderStatus = 1 OR o.orderStatus = 2 OR o.orderStatus = 3) " +
        "AND o.orderDatetime >= :startDate " +
        "AND co.clientId = :clientId")
    Long getTotalOptionPriceForLastThreeMonths(@Param("clientId") Long clientId, @Param("startDate") LocalDateTime startDate);

    Page<ClientOrder> findByClientId(long clientId, Pageable pageable);

    Boolean existsByOrder_OrderId(Long orderId);

}
