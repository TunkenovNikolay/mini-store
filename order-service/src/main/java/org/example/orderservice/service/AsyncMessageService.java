package org.example.orderservice.service;

import org.example.orderservice.domain.entity.async.AsyncMessage;

import java.util.List;

public interface AsyncMessageService {

    void saveMessage(AsyncMessage message);

    List<AsyncMessage> getUnsentOutboxMessages(int batchSize);

    void markAsSent(AsyncMessage message);
}
