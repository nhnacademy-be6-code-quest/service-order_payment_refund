package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetail;
import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOrderDetailOptionRepository extends JpaRepository<ProductOrderDetailOption, Long> {
    List<ProductOrderDetailOption> findByProductOrderDetail_ProductOrderDetailId(Long productOrderDetailId);
    Optional<ProductOrderDetailOption> findFirstByProductOrderDetail_ProductOrderDetailId(Long productOrderDetailId);
}
