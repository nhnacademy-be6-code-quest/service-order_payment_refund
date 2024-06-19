package com.nhnacademy.order.repository;

import com.nhnacademy.order.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
