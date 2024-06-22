package com.nhnacademy.orderpaymentrefund.repository.shipping;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingPolicyRepository extends JpaRepository<ShippingPolicy, Long> {
}
