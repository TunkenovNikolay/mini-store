package org.example.orderservice.service;

import org.example.orderservice.domain.aggregate.Order;
import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.domain.dto.OrderRequest;
import org.example.orderservice.integration.payment.dto.DeliveryStatus;
import org.example.orderservice.integration.payment.dto.PaymentStatus;

public interface OrderService {
    /**
     * Получить Order по orderId
     */
    Order getOrder(String orderId);

    /**
     * Создать Order
     */
    Order createOrder(OrderRequest request);

    /**
     * Обновить Order по orderId
     */
    Order updateOrder(String orderId, OrderDto orderDto);

    /**
     * Удалить Order по orderId
     */
    void deleteOrder(String orderId);

    /**
     * Обновить статус платежа для заказа
     *
     * @param orderRefId  идентификатор заказа
     * @param paymentStatus статус платежа
     */
    void updatePaymentStatus(String orderRefId, PaymentStatus paymentStatus);

    /**
     * Обновить статус доставки для заказа
     *
     * @param orderRefId  идентификатор платежа
     * @param deliveryStatus статус платежа
     */
    void updateDeliveryStatus(String orderRefId, DeliveryStatus deliveryStatus);
}
