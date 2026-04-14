package org.example.orderservice.schedule.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.domain.entity.async.AsyncMessage;
import org.example.orderservice.service.AsyncMessageService;
import org.example.orderservice.integration.delivery.dto.DeliveryRequestMessage;
import org.hibernate.service.spi.ServiceException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.util.Objects;

/**
 * Класс для обработки и отправки асинхронных сообщений через Kafka.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncMessageSenderProcessor {

    private final AsyncMessageService asyncMessageService;
    private final KafkaTemplate<String, DeliveryRequestMessage> kafkaTemplate;
    private final JsonMapper mapper;

    /**
     * Отправляет асинхронное сообщение через Kafka и обновляет его статус.
     * Оборачивается в транзакцию для обеспечения атомарности.
     *
     * @param message сообщение, которое необходимо отправить
     */
    @Transactional
    public void sendMessage(AsyncMessage message) {
        try {
            DeliveryRequestMessage reqMessage = mapper.readValue(message.getValue(), DeliveryRequestMessage.class);

            kafkaTemplate.send(message.getTopic(), Objects.requireNonNull(message.getId()).getId(), reqMessage)
                .exceptionally(e -> {
                    throw new ServiceException("Error on sending message '%s'".formatted(message), e);
                })
                .get();

            asyncMessageService.markAsSent(message);
        } catch (Exception e) {
            log.error("Error on sending message: {}", message, e);
        }
    }
}