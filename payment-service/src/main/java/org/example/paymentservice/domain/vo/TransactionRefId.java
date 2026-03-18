package org.example.paymentservice.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Ссылка на банк (транзакцию)
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRefId {
    private UUID TransactionRefId;
}
