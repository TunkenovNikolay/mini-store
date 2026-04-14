package org.example.orderservice.service;

import org.example.orderservice.domain.entity.async.AsyncMessage;

import java.util.List;

/**
 * Реализация сервиса для работы с асинхронными сообщениями.
 */
public interface AsyncMessageService {

    /**
     * Сохраняет сообщение в базе данных.
     *
     * @param message сообщение для сохранения
     */
    void saveMessage(AsyncMessage message);

    /**
     * Получает список неподтвержденных (неотправленных) сообщений с ограничением по размеру батча.
     *
     * @param batchSize максимальное количество сообщений
     * @return список неподтвержденных сообщений
     */
    List<AsyncMessage> getUnsentOutboxMessages(int batchSize);

    /**
     * Обновляет статус сообщения на "Отправлено" и сохраняет изменение.
     *
     * @param message сообщение, которое нужно пометить как отправленное
     */
    void markAsSent(AsyncMessage message);
}
