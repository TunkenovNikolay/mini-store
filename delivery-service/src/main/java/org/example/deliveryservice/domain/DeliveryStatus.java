package org.example.deliveryservice.domain;

public enum DeliveryStatus {

    PENDING,      // Ждёт обработки
    IN_TRANSIT,   // В пути
    DELIVERED,    // Доставлено
    FAILED,       // Ошибка доставки
    CANCELLED     // Отменено

}
