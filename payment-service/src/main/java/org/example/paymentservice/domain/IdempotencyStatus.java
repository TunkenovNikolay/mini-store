package org.example.paymentservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdempotencyStatus {

    PENDING, COMPLETED
}
