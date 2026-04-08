package org.example.orderservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    String customerId;
    BigDecimal amount;
    String currency;
    String productId;
    String street;
    String city;
    String postalCode;
}
