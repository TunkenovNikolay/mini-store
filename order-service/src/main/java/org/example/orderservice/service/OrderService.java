package org.example.orderservice.service;

import org.example.orderservice.domain.dto.OrderRequest;
import org.example.orderservice.domain.dto.OrderDto;

public interface OrderService {
    /**
     * Получить Order по orderId
     */
    OrderDto getOrder(String orderId);

    /**
     * Создать Order
     */
    OrderDto createOrder(OrderRequest request);

    /**
     * Обновить Order по orderId
     */
    OrderDto updateOrder(String orderId, OrderDto orderDto);

    /**
     * Удалить Order по orderId
     */
    void deleteOrder(String orderId);

}
