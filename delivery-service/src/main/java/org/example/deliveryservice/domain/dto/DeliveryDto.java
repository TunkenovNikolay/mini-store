package org.example.deliveryservice.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.example.deliveryservice.domain.DeliveryStatus;

import java.util.UUID;

@Data
@Builder
public class DeliveryDto {

    private Long id;
    private String deliveryId;
    private UUID orderRefId;
    private String paymentRefId;
    private String street;
    private String city;
    private String postalCode;
    private DeliveryStatus status;
    private String trackingNumber;

}
