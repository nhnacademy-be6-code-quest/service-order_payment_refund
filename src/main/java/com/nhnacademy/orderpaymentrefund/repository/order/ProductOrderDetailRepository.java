package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.Order;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductOrderDetailRepository extends JpaRepository<ProductOrderDetail, Long> {

    List<ProductOrderDetail> findAllByOrder(Order order);
    Optional<ProductOrderDetail> findFirstByOrder(Order order);

    @Query(value = "select count(o) from Order o where o.orderId = :orderId")
    long getSizeByOrderId(long orderId);
    
}
