package org.example.paymentservice.domain;

public enum PaymentStatus {
    PENDING,      // Ожидает обработки
    PROCESSING,   // Обрабатывается платёжной системой
    COMPLETED,    // Успешно оплачено
    FAILED,       // Ошибка оплаты
    REFUNDED      // Возврат средств
}
