package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderDetailOptionRepository extends JpaRepository<ProductOrderDetailOption, Long> {
}
