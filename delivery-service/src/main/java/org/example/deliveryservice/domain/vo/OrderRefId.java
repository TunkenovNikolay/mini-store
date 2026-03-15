package org.example.deliveryservice.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * orderId.orderId из order-service
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRefId {

    private UUID OrderRefId;

}
