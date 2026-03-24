package org.example.paymentservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.paymentservice.domain.entity.IdempotencyKey;
import org.example.paymentservice.repository.IdempotencyRepository;
import org.example.paymentservice.service.IdempotencyService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyRepository idempotencyRepository;

    @Override
    public void createPendingKey(String key) {

    }

    @Override
    public Optional<IdempotencyKey> getPendingKey(String key) {
        return idempotencyRepository.findById(key);
    }

    @Override
    public void markAsCompleted(String key, String responseData, int statusCode) {

    }
}
