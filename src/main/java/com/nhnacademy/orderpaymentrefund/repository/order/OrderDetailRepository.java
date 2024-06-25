package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrder(Order order);

    @Query("SELECT od.order.orderStatus FROM OrderDetail od WHERE od.id = :orderDetailId")
    OrderStatus findOrderStatusByOrderDetailId(@Param("orderDetailId") long orderDetailId);

}
