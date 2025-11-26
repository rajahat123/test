package com.ecommerce.orderservice.repository;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
}
