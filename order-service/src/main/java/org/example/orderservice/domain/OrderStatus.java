package org.example.orderservice.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    CREATED, CONFIRMED, SHIPPED, DELIVERED
}
