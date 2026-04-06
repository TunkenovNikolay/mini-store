package org.example.orderservice.integration.payment.amqp.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.service.payment")
public record RabbitMqPaymentServiceProperties(
    String exchangeRequestName,
    String queueRequestName
) {
}
