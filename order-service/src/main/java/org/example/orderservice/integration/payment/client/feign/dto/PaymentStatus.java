package org.example.orderservice.integration.payment.client.feign.dto;

public enum PaymentStatus {
    PENDING,      // Ожидает обработки
    PROCESSING,   // Обрабатывается платёжной системой
    COMPLETED,    // Успешно оплачено
    FAILED,       // Ошибка оплаты
    REFUNDED      // Возврат средств
}
