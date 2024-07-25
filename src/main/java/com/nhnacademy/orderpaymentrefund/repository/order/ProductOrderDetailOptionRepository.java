package com.nhnacademy.orderpaymentrefund.repository.order;

import com.nhnacademy.orderpaymentrefund.domain.order.ProductOrderDetailOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOrderDetailOptionRepository extends JpaRepository<ProductOrderDetailOption, Long> {
    List<ProductOrderDetailOption> findByProductOrderDetail_ProductOrderDetailId(Long productOrderDetailId);
    Optional<ProductOrderDetailOption> findFirstByProductOrderDetail_ProductOrderDetailId(Long productOrderDetailId);
}
