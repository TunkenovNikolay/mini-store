package org.example.deliveryservice.integration.order.dto;

import org.example.deliveryservice.domain.DeliveryStatus;

import java.io.Serializable;

public record DeliveryResponseMessage(
    String deliveryId,
    String orderRefId,
    String paymentRefId,
    String street,
    String city,
    String postalCode,
    DeliveryStatus status,
    String trackingNumber
) implements Serializable {
}
