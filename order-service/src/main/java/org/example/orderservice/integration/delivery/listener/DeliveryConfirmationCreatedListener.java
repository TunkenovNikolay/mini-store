package org.example.orderservice.integration.delivery.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.orderservice.integration.delivery.dto.DeliveryResponseMessage;
import org.example.orderservice.listener.IdempotentKafkaListener;
import org.example.orderservice.service.AsyncMessageService;
import org.example.orderservice.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@Slf4j
public class DeliveryConfirmationCreatedListener extends IdempotentKafkaListener<DeliveryResponseMessage> {

    private final OrderService orderService;

    public DeliveryConfirmationCreatedListener(AsyncMessageService asyncMessageService,
                                               JsonMapper mapper,
                                               OrderService orderService) {
        super(asyncMessageService, mapper);
        this.orderService = orderService;
    }

    @KafkaListener(
        topics = "${kafka.service.order.delivery-confirmation-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @Override
    public void consume(ConsumerRecord<String, DeliveryResponseMessage> consumerRecord,
                        DeliveryResponseMessage message,
                        Acknowledgment ack) throws JsonProcessingException {
        super.consume(consumerRecord, message, ack);
    }

    @Override
    public void processConsumedMessage(DeliveryResponseMessage message) {
        log.info("Consumed delivery confirmation created response message: {}", message);
        orderService.updateDeliveryStatus(message.orderRefId(), message.status());
        log.info("Updated order - {}, deliveryStatus = {}", message.orderRefId(), message.status());
    }
}