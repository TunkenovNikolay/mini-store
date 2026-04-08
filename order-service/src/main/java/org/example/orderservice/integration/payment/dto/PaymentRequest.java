package org.example.orderservice.integration.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequest {
    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private String currency;

}
