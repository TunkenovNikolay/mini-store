package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.controller.docs.OrderControllerDoc;
import org.example.orderservice.domain.dto.OrderRequest;
import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController implements OrderControllerDoc {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/{orderId}")
    @Override
    public ResponseEntity<OrderDto> get(@PathVariable String orderId) {
        log.info("GET orders/{}", orderId);
        final OrderDto orderDto = orderMapper.toOrderDto(orderService.getOrder(orderId));
        log.debug("GET orders/{}", orderDto);

        return ResponseEntity.ok(orderDto);
    }

    @PostMapping
    @Override
    public ResponseEntity<OrderDto> create(@RequestBody OrderRequest orderRequest) {
        log.info("POST orders - customerId: {}, amount: {}, currency: {}",
            orderRequest.getCustomerId(), orderRequest.getAmount(), orderRequest.getCurrency());
        final OrderDto newOrderDto = orderMapper.toOrderDto(orderService.createOrder(orderRequest));
        log.debug("POST orders/{}", newOrderDto);

        return ResponseEntity.ok(newOrderDto);
    }

    @PutMapping("/{orderId}")
    @Override
    public ResponseEntity<OrderDto> update(@PathVariable String orderId, @RequestBody OrderDto orderDto) {
        log.info("PUT orders/{}", orderId);
        final OrderDto updatedOrder = orderMapper.toOrderDto(orderService.updateOrder(orderId, orderDto));
        log.debug("PUT orders/{}", updatedOrder);

        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable String orderId) {
        log.info("DELETE orders/{}", orderId);
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}
