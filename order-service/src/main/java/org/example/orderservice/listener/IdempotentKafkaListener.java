package org.example.orderservice.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.example.orderservice.domain.entity.async.AsyncMessage;
import org.example.orderservice.domain.enums.AsyncMessageStatus;
import org.example.orderservice.domain.enums.AsyncMessageType;
import org.example.orderservice.service.AsyncMessageService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.support.Acknowledgment;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;

/**
 * Абстрактный класс для обработки Kafka сообщений с обеспечением идемпотентности.
 *
 * @param <T> Тип сообщения
 */
@RequiredArgsConstructor
@Slf4j
public abstract class IdempotentKafkaListener<T> {

    private final AsyncMessageService asyncMessageService;
    private final JsonMapper mapper;

    /**
     * Обработка полученного Kafka сообщения с проверкой идемпотентности.
     *
     * @param consumerRecord исходный Kafka рекорд
     * @param message        расшифрованное сообщение
     * @param acknowledgment подтверждение обработки
     * @throws JsonProcessingException при ошибке сериализации сообщения
     */
    public void consume(ConsumerRecord<String, T> consumerRecord,
                        T message,
                        Acknowledgment acknowledgment) throws JsonProcessingException {
        // Получение заголовка с ключом идемпотентности
        Header idempotentKeyHeader = consumerRecord.headers().lastHeader("X-Idempotency-Key");
        if (idempotentKeyHeader == null) {
            log.error("Idempotent key header is null for consumer record " + consumerRecord);
            acknowledgment.acknowledge();
            return;
        }

        // Преобразование заголовка в строку
        String idempotentKey = new String(idempotentKeyHeader.value(), StandardCharsets.UTF_8);

        // Создание объекта для хранения в базе данных
        AsyncMessage asyncConsumedMessage = AsyncMessage.builder()
            .id(idempotentKey)
            .topic(consumerRecord.topic())
            .value(mapper.writeValueAsString(message))
            .status(AsyncMessageStatus.RECEIVED)
            .type(AsyncMessageType.INBOX)
            .build();

        try {
            // Сохранение сообщения для проверки идемпотентности
            asyncMessageService.saveMessage(asyncConsumedMessage);
        } catch (DataIntegrityViolationException ex) {
            // Если сообщение с таким ключом уже есть — логируем и подтверждаем
            log.warn("Message with the same idempotent key is present in DB: {}", idempotentKey);
            acknowledgment.acknowledge();
            return;
        }

        // Обработка сообщения
        processConsumedMessage(message);
        acknowledgment.acknowledge();
    }

    /**
     * Метод для обработки конкретных сообщений, реализуемый в подклассах.
     *
     * @param message полученное сообщение
     */
    public abstract void processConsumedMessage(T message);
}