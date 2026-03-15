package org.example.paymentservice.service;

import org.example.paymentservice.domain.dto.CreatePaymentRequest;
import org.example.paymentservice.domain.dto.PaymentDto;

public interface PaymentService {
    /**
     * Получить Payment по paymentId
     */
    PaymentDto getPayment(String paymentId);

    /**
     * Создать Payment
     */
    PaymentDto createPayment(CreatePaymentRequest request);

    /**
     * Обновить Payment по paymentId
     */
    PaymentDto updatePayment(String paymentId, PaymentDto paymentDto);

    /**
     * Удалить Payment по paymentId
     */
    void deletePayment(String paymentId);

}

