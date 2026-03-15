package org.example.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.domain.dto.CreateOrderRequest;
import org.example.orderservice.domain.dto.OrderDto;
import org.example.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> get(@PathVariable String orderId) {
        log.info("GET orders/{}", orderId);
        final OrderDto orderDto = orderService.getOrder(orderId);
        log.debug("GET orders/{}", orderDto);

        return ResponseEntity.ok(orderDto);
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody CreateOrderRequest createOrderRequest) {
        log.info("POST orders - customerId: {}, amount: {}, currency: {}",
            createOrderRequest.getCustomerId(), createOrderRequest.getAmount(), createOrderRequest.getCurrency());
        final OrderDto newOrderDto = orderService.createOrder(createOrderRequest);
        log.debug("POST orders/{}", newOrderDto);

        return ResponseEntity.ok(newOrderDto);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> update(@PathVariable String orderId, @RequestBody OrderDto orderDto) {
        log.info("PUT orders/{}", orderId);
        final OrderDto updatedOrder = orderService.updateOrder(orderId, orderDto);
        log.debug("PUT orders/{}", updatedOrder);

        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable String orderId) {
        log.info("DELETE orders/{}", orderId);
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}
