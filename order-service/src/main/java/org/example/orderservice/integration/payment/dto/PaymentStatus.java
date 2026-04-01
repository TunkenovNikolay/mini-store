package org.example.orderservice.integration.payment.dto;

public enum PaymentStatus {
    PENDING,      // Ожидает обработки
    PROCESSING,   // Обрабатывается платёжной системой
    COMPLETED,    // Успешно оплачено
    FAILED,       // Ошибка оплаты
    REFUNDED      // Возврат средств
}
