package org.example.orderservice.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    CREATED, CONFIRMED, SHIPPED, DELIVERED
}
