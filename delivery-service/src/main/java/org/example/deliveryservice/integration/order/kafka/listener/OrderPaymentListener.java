package org.example.deliveryservice.integration.order.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.deliveryservice.domain.aggregate.Delivery;
import org.example.deliveryservice.integration.order.dto.DeliveryRequestMessage;
import org.example.deliveryservice.mapper.DeliveryMapper;
import org.example.deliveryservice.service.DeliveryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Класс слушателя Kafka для обработки сообщений об оплаченных заказах.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderPaymentListener {

    private final DeliveryService deliveryService;
    private final DeliveryMapper deliveryMapper;

    /**
     * Метод обработки сообщения о подтверждении оплаты заказа из Kafka.
     *
     * @param consumerRecord исходное сообщение Kafka
     * @param message        DTO с данными о забронированной встрече
     * @param ack            механизм подтверждения обработки сообщения
     */
    @KafkaListener(
        topics = "${kafka.service.delivery.order-payment-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<String, DeliveryRequestMessage> consumerRecord,
                        DeliveryRequestMessage message,
                        Acknowledgment ack) {
        log.info("Received order message with ID: {}", message.orderRefId());
        Delivery delivery = deliveryService.createDelivery(deliveryMapper.toDelivery(message));
        log.info("Created insurance confirmation with ID: {}", delivery.getId());

        ack.acknowledge();
        log.info("Appointment message successfully processed and acknowledged");
    }
}