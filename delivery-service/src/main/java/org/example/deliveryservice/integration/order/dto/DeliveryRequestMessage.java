package org.example.deliveryservice.integration.order.dto;

import java.io.Serializable;

public record DeliveryRequestMessage(
    String orderRefId,
    String paymentRefId,
    String street,
    String city,
    String postalCode,
    String trackingNumber
) implements Serializable {
}
