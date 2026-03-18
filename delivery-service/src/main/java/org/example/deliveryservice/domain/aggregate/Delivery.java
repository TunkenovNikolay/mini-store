package org.example.deliveryservice.domain.aggregate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.deliveryservice.domain.DeliveryStatus;
import org.example.deliveryservice.domain.vo.DeliveryAddress;
import org.example.deliveryservice.domain.vo.DeliveryId;
import org.example.deliveryservice.domain.vo.OrderRefId;
import org.example.deliveryservice.domain.vo.PaymentRefId;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Embedded
    @Column(nullable = false, unique = true)
    private DeliveryId deliveryId = new DeliveryId();

    @Embedded
    @Column(nullable = false)
    private OrderRefId orderRefId;

    @Embedded
    @Column(nullable = false)
    private PaymentRefId paymentRefId;

    @Embedded
    @Column(nullable = false)
    private DeliveryAddress address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    @Column(columnDefinition = "text")
    private String trackingNumber;

    public Delivery(OrderRefId orderRefId, PaymentRefId paymentRefId, DeliveryAddress address) {
        this.orderRefId = orderRefId;
        this.paymentRefId = paymentRefId;
        this.address = address;
    }

}
