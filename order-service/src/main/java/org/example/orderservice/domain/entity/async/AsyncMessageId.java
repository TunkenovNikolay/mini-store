package org.example.orderservice.domain.entity.async;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AsyncMessageId implements Serializable {

    private String id;

    private String topic;
}
