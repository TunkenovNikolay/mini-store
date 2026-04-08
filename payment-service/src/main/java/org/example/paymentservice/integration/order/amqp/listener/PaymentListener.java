package org.example.paymentservice.integration.order.amqp.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.domain.aggregate.Payment;
import org.example.paymentservice.integration.order.amqp.config.properties.RabbitMqOrderServiceProperties;
import org.example.paymentservice.integration.order.dto.request.PaymentRequestMessage;
import org.example.paymentservice.integration.order.dto.response.PaymentResponseMessage;
import org.example.paymentservice.mapper.PaymentMapper;
import org.example.paymentservice.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Слушатель сообщений для оплаты заказов
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentListener {

    private final PaymentService paymentService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqOrderServiceProperties rabbitMqOrderServiceProperties;
    private final PaymentMapper paymentMapper;

    /**
     * Обрабатывает входящее сообщение с запросом оплаты заказа
     *
     * @param paymentRequestMessage сообщение с данными оплаты заказа
     */
    @RabbitListener(queues = "payment-request-queue")
    public void handle(final PaymentRequestMessage paymentRequestMessage) {
        log.info("receive payment request: {}", paymentRequestMessage);
        //Вызов сервиса для оплаты заказа
        Payment payment = paymentService.createPayment(paymentMapper.toCreatePaymentRequest(paymentRequestMessage));

        //Отправка ответа по принятии запроса на оплату
        PaymentResponseMessage paymentResponseMessage = paymentMapper.toPaymentResponseMessage(payment);
        rabbitTemplate.convertAndSend(
            rabbitMqOrderServiceProperties.exchangeResponseName(),
            rabbitMqOrderServiceProperties.exchangeResponseName(),
            paymentResponseMessage
        );
        log.info("Sent payment response for orderId={}", paymentRequestMessage.orderId());
    }
}
