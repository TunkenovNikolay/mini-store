package org.example.deliveryservice.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryId {

    private String deliveryId = "DEL-" + UUID.randomUUID();

}
