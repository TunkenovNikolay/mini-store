package org.example.paymentservice.service;

import org.example.paymentservice.domain.aggregate.Payment;
import org.example.paymentservice.domain.dto.CreatePaymentRequest;
import org.example.paymentservice.domain.dto.PaymentDto;

public interface PaymentService {
    /**
     * Получить Payment по paymentId
     */
    Payment getPayment(String paymentId);

    /**
     * Создать Payment
     */
    Payment createPayment(CreatePaymentRequest request);

    /**
     * Обновить Payment по paymentId
     */
    Payment updatePayment(String paymentId, PaymentDto paymentDto);

    /**
     * Удалить Payment по paymentId
     */
    void deletePayment(String paymentId);

}

