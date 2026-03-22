package org.example.paymentservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.paymentservice.domain.IdempotencyStatus;

import java.time.ZonedDateTime;

@Entity
@Table(name = "idempotency_keys")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "key")
@ToString
@Getter
@Setter
public class IdempotencyKey {

    @Id
    @Column(name = "key_value")
    private String key;

    @Enumerated(EnumType.STRING)
    private IdempotencyStatus status;

    @Lob
    private String response;

    private ZonedDateTime created;

    private int statusCode;

    public IdempotencyKey(String key, IdempotencyStatus status) {
        this.key = key;
        this.status = status;
        this.created = ZonedDateTime.now();
    }
}
