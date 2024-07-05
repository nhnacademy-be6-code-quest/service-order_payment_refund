package com.nhnacademy.orderpaymentrefund.repository.shipping;

import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicy;
import com.nhnacademy.orderpaymentrefund.domain.shipping.ShippingPolicyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingPolicyRepository extends JpaRepository<ShippingPolicy, Long> {
    Optional<ShippingPolicy> findByShippingPolicyType(ShippingPolicyType shippingPolicyType);
}
