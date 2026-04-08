package org.example.deliveryservice.domain.dto;

import lombok.Data;

@Data
public class CreateDeliveryRequest {

    private String orderRefId;
    private String paymentRefId;
    private String street;
    private String city;
    private String postalCode;

}
