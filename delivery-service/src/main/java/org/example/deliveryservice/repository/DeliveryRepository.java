package org.example.deliveryservice.repository;

import org.example.deliveryservice.domain.aggregate.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByDeliveryId_deliveryId(String deliveryId);
    Optional<Delivery> findByOrderRefId_orderRefId(UUID orderRefId);

}
