package org.example.orderservice.service;

import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.domain.dto.OrderRequest;
import org.example.orderservice.integration.payment.dto.PaymentStatus;

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

    /**
     * Обновить статус платежа для заказа
     *
     * @param inquiryRefId  идентификатор платежа
     * @param paymentStatus статус платежа
     */
    void updatePaymentStatus(String inquiryRefId, PaymentStatus paymentStatus);
}
