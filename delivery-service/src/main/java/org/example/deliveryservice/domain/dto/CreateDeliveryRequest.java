package org.example.deliveryservice.domain.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateDeliveryRequest {

    private UUID orderRefId;
    private String paymentRefId;
    private String street;
    private String city;
    private String postalCode;

}
