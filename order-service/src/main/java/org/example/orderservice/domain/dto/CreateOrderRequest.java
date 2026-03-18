package org.example.orderservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequest {
    String customerId;
    BigDecimal amount;
    String currency;
    String productId;
}
