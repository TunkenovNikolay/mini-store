package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.domain.OrderStatus;
import org.example.orderservice.domain.aggregate.Order;
import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.domain.dto.OrderRequest;
import org.example.orderservice.domain.vo.CustomerId;
import org.example.orderservice.domain.vo.Money;
import org.example.orderservice.domain.vo.ProductId;
import org.example.orderservice.exception.ErrorMessage;
import org.example.orderservice.exception.ServiceException;
import org.example.orderservice.integration.payment.client.feign.PaymentFeignClientImpl;
import org.example.orderservice.integration.payment.client.feign.dto.CreatePaymentRequest;
import org.example.orderservice.integration.payment.client.feign.dto.PaymentDto;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentFeignClientImpl paymentFeignClientImpl;

    @Override
    public OrderDto getOrder(String orderId) {
        return orderRepository.findByOrderId_OrderId(orderId)
            .map(orderMapper::toOrderDto)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderId));
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderRequest request) {

        CustomerId customerId = new CustomerId(request.getCustomerId());
        Money money = new Money(request.getAmount(), request.getCurrency());
        ProductId productId = new ProductId(request.getProductId());

        Order order = new Order(money, customerId, productId);
        Order savedOrder = orderRepository.save(order);

        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
            .inquiryRefId(savedOrder.getOrderId().getOrderId())
            .amount(request.getAmount())
            .currency(request.getCurrency())
            .build();

        PaymentDto payment = paymentFeignClientImpl.createPayment(order.getOrderId().getOrderId(), paymentRequest);

        savedOrder.setPaymentId(payment.getPaymentId());
        savedOrder.setPaymentStatus(payment.getStatus());
        orderRepository.save(savedOrder);

        return orderMapper.toOrderDto(savedOrder);
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

}
