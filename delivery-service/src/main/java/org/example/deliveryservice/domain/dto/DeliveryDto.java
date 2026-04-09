package org.example.deliveryservice.domain.dto;

import lombok.Builder;
import lombok.Data;
import org.example.deliveryservice.domain.DeliveryStatus;

@Data
@Builder
public class DeliveryDto {

    private Long id;
    private String deliveryId;
    private String orderRefId;
    private String paymentRefId;
    private String street;
    private String city;
    private String postalCode;
    private DeliveryStatus status;
    private String trackingNumber;

}
