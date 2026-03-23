package org.example.orderservice.integration.payment.client.feign.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CreatePaymentRequest {
    private UUID inquiryRefId;
    private BigDecimal amount;
    private String currency;

}
