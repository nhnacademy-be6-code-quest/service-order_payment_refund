package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.NonClientOrder;
import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NonClientOrderRepository extends JpaRepository<NonClientOrder, Long> {

    Optional<NonClientOrder> findByOrder_OrderId(Long orderId);

    Optional<NonClientOrder> findByNonClientOrderPasswordEqualsAndOrder_OrderCodeEquals(
        String nonClientOrderPassword, String orderCode);

    Boolean existsByOrder_OrderId(Long orderId);


    @Query("SELECT nco FROM NonClientOrder nco WHERE nco.nonClientOrdererName = :nonClientOrdererName " +
        "AND nco.nonClientOrdererEmail = :nonClientOrdererEmail " +
        "AND nco.order.phoneNumber = :phoneNumber " +
        "ORDER BY nco.order.orderDatetime DESC")
    List<NonClientOrder> findRecent10OrderNonClientOrder(
        @Param("nonClientOrdererName") String nonClientOrdererName,
        @Param("nonClientOrdererEmail") String nonClientOrdererEmail,
        @Param("phoneNumber") String phoneNumber);

}
