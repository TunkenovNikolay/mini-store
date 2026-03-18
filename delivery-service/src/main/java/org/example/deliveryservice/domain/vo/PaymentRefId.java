package org.example.deliveryservice.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * paymentId из payment-service
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefId {

    private String paymentRefId;

}
