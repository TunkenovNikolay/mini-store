package org.example.paymentservice.service;

import org.example.paymentservice.domain.entity.IdempotencyKey;

import java.util.Optional;

public interface IdempotencyService {

    void createPendingKey(String key);

    Optional<IdempotencyKey> getPendingKey(String key);

    void markAsCompleted(String key, String responseData, int statusCode);
}
