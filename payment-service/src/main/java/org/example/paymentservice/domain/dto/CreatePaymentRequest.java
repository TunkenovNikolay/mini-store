package org.example.paymentservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentRequest {
    private UUID inquiryRefId;
    private BigDecimal amount;
    private String currency;

}
