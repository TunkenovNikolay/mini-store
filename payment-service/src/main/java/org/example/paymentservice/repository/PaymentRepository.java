package org.example.paymentservice.repository;

import org.example.paymentservice.domain.aggregate.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentId_PaymentId(String PaymentId);

}
