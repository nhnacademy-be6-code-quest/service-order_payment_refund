package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductOrderDetailRepository extends JpaRepository<ProductOrderDetail, Long> {

    List<ProductOrderDetail> findAllByOrder_OrderId(Long orderId);
    Optional<ProductOrderDetail> findFirstByOrder_OrderId(Long orderId);

    @Query(value = "select count(o) from ProductOrderDetail o where o.order = :order")
    long getSizeByOrderId(Order order);
}
