package org.example.paymentservice.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.example.paymentservice.domain.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PaymentDto {
    private Long id;
    private String paymentId;
    private UUID inquiryRefId;
    private BigDecimal amount;
    private String currency;
    private UUID transactionRefId;
    private PaymentStatus status;

}
