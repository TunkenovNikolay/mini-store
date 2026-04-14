package org.example.orderservice.integration.payment.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.orderservice.integration.delivery.dto.DeliveryResponseMessage;
import org.example.orderservice.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Класс слушателя Kafka для обработки сообщений о доставке
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryListener {

    private final OrderService orderService;

    /**
     * Метод обработки сообщения о назначении доставки из Kafka.
     *
     * @param consumerRecord исходное сообщение Kafka
     * @param message        DTO с данными о забронированной встрече
     * @param ack            механизм подтверждения обработки сообщения
     */
    @KafkaListener(
        topics = "${kafka.service.order.delivery-confirmation-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<String, DeliveryResponseMessage> consumerRecord,
                        DeliveryResponseMessage message,
                        Acknowledgment ack) {
        log.info("Received delivery message with ID: {}", message.deliveryId());
        orderService.updateDeliveryStatus(message.orderRefId(), message.status());
        log.info("Created delivery confirmation with ID: {}", message.deliveryId());

        ack.acknowledge();
        log.info("Order message successfully processed and acknowledged");
    }
}