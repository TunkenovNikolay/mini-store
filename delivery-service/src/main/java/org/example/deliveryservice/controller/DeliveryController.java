package org.example.deliveryservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.deliveryservice.domain.dto.CreateDeliveryRequest;
import org.example.deliveryservice.domain.dto.DeliveryDto;
import org.example.deliveryservice.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDto> get(@PathVariable String deliveryId) {
        log.info("GET delivery/{}", deliveryId);
        final DeliveryDto deliveryDto = deliveryService.getDeliveryByDeliveryId(deliveryId);
        log.debug("GET /delivery/ - deliveryDto: {}", deliveryDto);

        return ResponseEntity.ok(deliveryDto);
    }

    @GetMapping("/order/{orderRefId}")
    public ResponseEntity<DeliveryDto> get(@PathVariable UUID orderRefId) {
        log.info("GET /order/delivery/{}", orderRefId);
        final DeliveryDto deliveryDto = deliveryService.getDeliveryByOrderRefId(orderRefId);
        log.debug("GET /order/delivery/ - deliveryDto: {}", deliveryDto);

        return ResponseEntity.ok(deliveryDto);
    }

    @PostMapping
    public ResponseEntity<DeliveryDto> create(@RequestBody CreateDeliveryRequest createDeliveryRequest) {
        log.info("POST delivery - orderRefId: {}, paymentRefId: {}, street: {}, city: {}, postalCode: {}",
            createDeliveryRequest.getOrderRefId(),
            createDeliveryRequest.getPaymentRefId(),
            createDeliveryRequest.getStreet(),
            createDeliveryRequest.getCity(),
            createDeliveryRequest.getPostalCode());
        final DeliveryDto newDeliveryDto = deliveryService.createDelivery(createDeliveryRequest);
        log.debug("POST delivery - created: {}", newDeliveryDto);

        return ResponseEntity.ok(newDeliveryDto);
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<DeliveryDto> update(@PathVariable String deliveryId, @RequestBody DeliveryDto deliveryDto) {
        log.info("PUT orders/{}", deliveryId);
        final DeliveryDto updatedDelivery = deliveryService.updateDelivery(deliveryId, deliveryDto);
        log.debug("PUT delivery - {}", updatedDelivery);

        return ResponseEntity.ok(updatedDelivery);
    }

    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<Void> delete(@PathVariable String deliveryId) {
        log.info("DELETE delivery/{}", deliveryId);
        deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.noContent().build();
    }
}
