package org.example.orderservice.domain.aggregate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.orderservice.domain.OrderStatus;
import org.example.orderservice.domain.vo.CustomerId;
import org.example.orderservice.domain.vo.Money;
import org.example.orderservice.domain.vo.OrderId;
import org.example.orderservice.domain.vo.ProductId;

import java.math.BigDecimal;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Embedded
    @Column(nullable = false, unique = true)
    private OrderId orderId = new OrderId();

    @Embedded
    @Column(nullable = false)
    private Money money = new Money(BigDecimal.ZERO, "USD");

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CREATED;

    @Embedded
    @Column(nullable = false)
    private CustomerId customerId;

    @Embedded
    @Column(nullable = false)
    private ProductId productId;

    public Order(Money money, CustomerId customerId, ProductId productId) {
        this.money = money != null ? money : new Money(BigDecimal.ZERO, "USD");
        this.customerId = customerId;
        this.productId = productId;
    }

}
