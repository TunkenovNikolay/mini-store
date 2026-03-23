package org.example.orderservice.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderId {
    private UUID orderId = UUID.randomUUID(); // Храним чистый UUID

    @Override
    public String toString() {
        return "ORD-" + orderId.toString(); // Только для отображения
    }

}
