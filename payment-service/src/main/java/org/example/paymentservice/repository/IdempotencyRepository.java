package org.example.paymentservice.repository;

import jakarta.persistence.LockModeType;
import org.example.paymentservice.domain.entity.IdempotencyKey;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdempotencyRepository extends JpaRepository<IdempotencyKey, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IdempotencyKey> findById(@Nullable String key);

}
