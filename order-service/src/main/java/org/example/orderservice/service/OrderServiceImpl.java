package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.domain.aggregate.Order;
import org.example.orderservice.domain.dto.CreateOrderRequest;
import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.domain.vo.CustomerId;
import org.example.orderservice.domain.vo.Money;
import org.example.orderservice.domain.OrderStatus;
import org.example.orderservice.domain.vo.ProductId;
import org.example.orderservice.exception.ErrorMessage;
import org.example.orderservice.exception.ServiceException;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto getOrder(String orderId) {
        return orderRepository.findByOrderId_OrderId(orderId)
            .map(orderMapper::toOrderDto)
            .orElseThrow(() -> new ServiceException(ErrorMessage.ORDER_NOT_EXIST, orderId));
    }

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {

        CustomerId customerId = new CustomerId(request.getCustomerId());
        Money money = new Money(request.getAmount(), request.getCurrency());
        ProductId productId = new ProductId(request.getProductId());

        Order order = new Order(money, customerId, productId);

        Order savedOrder = orderRepository.save(order);

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
