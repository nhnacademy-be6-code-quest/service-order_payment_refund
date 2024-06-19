package com.nhnacademy.order.repository;

import com.nhnacademy.order.domain.shipping.ShippingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingPolicyRepository extends JpaRepository<ShippingPolicy, Long> {
}
