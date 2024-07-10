package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderDetailOptionRepository extends JpaRepository<ProductOrderDetailOption, Long> {
    List<ProductOrderDetailOption> findByProductOrderDetail(ProductOrderDetail productOrderDetail);
    ProductOrderDetailOption findFirstByProductOrderDetail(ProductOrderDetail productOrderDetail);
}
