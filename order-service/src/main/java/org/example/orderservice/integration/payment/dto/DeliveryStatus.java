package org.example.orderservice.integration.payment.dto;

public enum DeliveryStatus {

    PENDING,      // Ждёт обработки
    IN_TRANSIT,   // В пути
    DELIVERED,    // Доставлено
    FAILED,       // Ошибка доставки
    CANCELLED     // Отменено

}
