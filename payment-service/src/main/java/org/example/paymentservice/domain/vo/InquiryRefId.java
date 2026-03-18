package org.example.paymentservice.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Ссылка на заказ из order-service
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRefId {
    private UUID InquiryRefId;
}
