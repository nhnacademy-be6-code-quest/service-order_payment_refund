package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderDetailRepository extends JpaRepository<ProductOrderDetail, Long> {
}
