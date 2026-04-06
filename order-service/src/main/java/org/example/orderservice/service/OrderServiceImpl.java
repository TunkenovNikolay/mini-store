package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.domain.OrderStatus;
import org.example.orderservice.domain.aggregate.Order;
import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.domain.dto.OrderRequest;
import org.example.orderservice.domain.vo.CustomerId;
import org.example.orderservice.domain.vo.Money;
import org.example.orderservice.domain.vo.ProductId;
import org.example.orderservice.exception.ErrorMessage;
import org.example.orderservice.exception.ServiceException;
import org.example.orderservice.integration.payment.amqp.config.properties.RabbitMqPaymentServiceProperties;
import org.example.orderservice.integration.payment.dto.PaymentRequest;
import org.example.orderservice.integration.payment.dto.PaymentStatus;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Override
    public OrderDto getOrder(String orderId) {
        return orderRepository.findByOrderId_OrderId(orderId)
            .map(orderMapper::toOrderDto)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderId));
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderRequest request) {
        Order order = createAndSaveOrder(request);
        sendPayment(order);

        return orderMapper.toOrderDto(order);
    }

    private Order createAndSaveOrder(OrderRequest request) {
        CustomerId customerId = new CustomerId(request.getCustomerId());
        Money money = new Money(request.getAmount(), request.getCurrency());
        ProductId productId = new ProductId(request.getProductId());

        Order order = new Order(money, customerId, productId);
        order.setPaymentStatus(PaymentStatus.PENDING);

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
    public OrderDto updateOrder(String orderId, OrderDto orderDto) {
        Order existingOrder = orderRepository.findByOrderId_OrderId(orderId)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderId));

        Money money = new Money(orderDto.getAmount(), orderDto.getCurrency());

        existingOrder.setMoney(money);
        existingOrder.setStatus(OrderStatus.valueOf(orderDto.getStatus()));

        return orderMapper.toOrderDto(orderRepository.save(existingOrder));
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
    }

}
