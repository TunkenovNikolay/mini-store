package org.example.orderservice.integration.payment.dto;

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
