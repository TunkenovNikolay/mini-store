package org.example.paymentservice.integration.order.dto.response;

import org.example.paymentservice.integration.order.dto.PaymentStatus;

import java.io.Serializable;
import java.math.BigDecimal;

public record PaymentResponseMessage(
    String orderId,
    String customerId,
    BigDecimal amount,
    String currency,
    PaymentStatus paymentStatus,
    String paymentId
) implements Serializable {
}
