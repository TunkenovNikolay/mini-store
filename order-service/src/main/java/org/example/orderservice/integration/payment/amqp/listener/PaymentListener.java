package org.example.orderservice.integration.payment.amqp.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.integration.payment.dto.PaymentResponseMessage;
import org.example.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Слушатель сообщений для оплаты заказов
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentListener {

    private final OrderService orderService;

    /**
     * Обрабатывает входящее сообщение с запросом оплаты заказа
     *
     * @param paymentResponseMessage сообщение с данными оплаты заказа
     */
    @RabbitListener(queues = "${rabbitmq.service.payment.queue-response-name}")
    public void handle(final PaymentResponseMessage paymentResponseMessage) {
        log.info("Received payment request: {}", paymentResponseMessage);
        orderService.updatePaymentStatus(paymentResponseMessage.orderId(), paymentResponseMessage.paymentStatus());
        log.info("Successfully updated order status for orderId={}", paymentResponseMessage.orderId());
    }
}
