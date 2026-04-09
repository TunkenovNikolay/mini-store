package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.domain.OrderStatus;
import org.example.orderservice.domain.aggregate.Order;
import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.domain.dto.OrderRequest;
import org.example.orderservice.domain.vo.CustomerId;
import org.example.orderservice.domain.vo.DeliveryAddress;
import org.example.orderservice.domain.vo.Money;
import org.example.orderservice.domain.vo.ProductId;
import org.example.orderservice.exception.ErrorMessage;
import org.example.orderservice.exception.ServiceException;
import org.example.orderservice.integration.payment.amqp.config.properties.RabbitMqPaymentServiceProperties;
import org.example.orderservice.integration.payment.dto.DeliveryRequestMessage;
import org.example.orderservice.integration.payment.dto.DeliveryStatus;
import org.example.orderservice.integration.payment.dto.PaymentRequest;
import org.example.orderservice.integration.payment.dto.PaymentStatus;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqPaymentServiceProperties rabbitMqPaymentServiceProperties;
    private final KafkaTemplate<String, DeliveryRequestMessage> kafkaTemplate;

    @Value("${kafka.service.order.order-payment-topic}")
    private String appointmentBookedTopic;

    @Override
    public Order getOrder(String orderId) {
        return orderRepository.findByOrderId_OrderId(orderId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderId));
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = createAndSaveOrder(request);
        sendPayment(order);

        return order;
    }

    private Order createAndSaveOrder(OrderRequest request) {
        CustomerId customerId = new CustomerId(request.getCustomerId());
        Money money = new Money(request.getAmount(), request.getCurrency());
        ProductId productId = new ProductId(request.getProductId());
        DeliveryAddress deliveryAddress = new DeliveryAddress(
            request.getStreet(),
            request.getCity(),
            request.getPostalCode());

        Order order = new Order(money, customerId, productId, deliveryAddress);
        order.setPaymentStatus(PaymentStatus.PROCESSING);

        return orderRepository.save(order);
    }

    private void sendPayment(Order order) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
            .orderId(order.getOrderId().getOrderId().toString())
            .customerId(order.getCustomerId().getCustomerId())
            .amount(order.getMoney().getAmount())
            .currency(order.getMoney().getCurrency())
            .build();

        rabbitTemplate.convertAndSend(
            rabbitMqPaymentServiceProperties.exchangeRequestName(),
            rabbitMqPaymentServiceProperties.exchangeRequestName(),
            paymentRequest
        );

        log.info("Sent payment request for orderId={}",
            order.getId());
    }


    @Override
    public Order updateOrder(String orderId, OrderDto orderDto) {
        Order existingOrder = orderRepository.findByOrderId_OrderId(orderId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderId));

        Money money = new Money(orderDto.getAmount(), orderDto.getCurrency());

        existingOrder.setMoney(money);
        existingOrder.setStatus(OrderStatus.valueOf(orderDto.getStatus()));

        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(String orderId) {
        Order existingOrder = orderRepository.findByOrderId_OrderId(orderId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderId));

        orderRepository.delete(existingOrder);
    }

    @Override
    public void updatePaymentStatus(String orderId, PaymentStatus paymentStatus) {
        Order order = orderRepository.findByOrderId_OrderId(orderId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderId));
        order.setPaymentStatus(paymentStatus);

        orderRepository.save(order);

        if (paymentStatus == PaymentStatus.COMPLETED) {
            sendOrderPaymentMessage(order);
        }
    }

    private void sendOrderPaymentMessage(Order order) {
        DeliveryRequestMessage deliveryRequestMessage = orderMapper.toDeliveryRequestMessage(order);
        kafkaTemplate.send(appointmentBookedTopic, order.getId().toString(), deliveryRequestMessage);
    }

    @Override
    public void updateDeliveryStatus(String orderRefId, DeliveryStatus deliveryStatus) {
        Order order = orderRepository.findByOrderId_OrderId(orderRefId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderRefId));
        order.setDeliveryStatus(deliveryStatus);

        orderRepository.save(order);
    }

}
