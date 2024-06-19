package com.nhnacademy.order.repository;

import com.nhnacademy.order.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClientId(long clientId);
}
