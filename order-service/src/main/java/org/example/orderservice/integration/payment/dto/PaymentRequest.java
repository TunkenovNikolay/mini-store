package org.example.orderservice.integration.payment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentRequest {
    private UUID inquiryRefId;
    private BigDecimal amount;
    private String currency;

}
