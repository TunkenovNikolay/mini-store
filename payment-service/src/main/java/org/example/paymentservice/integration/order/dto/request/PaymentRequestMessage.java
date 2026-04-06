package org.example.paymentservice.integration.order.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

public record PaymentRequestMessage(
    String orderId,
    String customerId,
    BigDecimal amount,
    String currency
) implements Serializable {
}
