package org.example.orderservice.integration.payment.dto;

import java.io.Serializable;

public record DeliveryRequestMessage(
    String orderRefId,
    String paymentRefId,
    String street,
    String city,
    String postalCode
) implements Serializable {
}
