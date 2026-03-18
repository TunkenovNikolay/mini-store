package org.example.orderservice.repository;

import org.example.orderservice.domain.aggregate.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId_OrderId(String orderId);

}
