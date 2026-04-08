package org.example.deliveryservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.deliveryservice.controller.docs.DeliveryControllerDoc;
import org.example.deliveryservice.domain.dto.CreateDeliveryRequest;
import org.example.deliveryservice.domain.dto.DeliveryDto;
import org.example.deliveryservice.mapper.DeliveryMapper;
import org.example.deliveryservice.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryControllerDoc {

    private final DeliveryService deliveryService;
    private final DeliveryMapper deliveryMapper;

    @GetMapping("/{deliveryId}")
    @Override
    public ResponseEntity<DeliveryDto> get(@PathVariable String deliveryId) {
        log.info("GET delivery/{}", deliveryId);
        final DeliveryDto deliveryDto = deliveryMapper.toDeliveryDto(deliveryService.getDeliveryByDeliveryId(deliveryId));
        log.debug("GET /delivery/ - deliveryDto: {}", deliveryDto);

        return ResponseEntity.ok(deliveryDto);
    }

    @GetMapping("/order/{orderRefId}")
    @Override
    public ResponseEntity<DeliveryDto> get(@PathVariable UUID orderRefId) {
        log.info("GET /delivery/order/{}", orderRefId);
        final DeliveryDto deliveryDto = deliveryMapper.toDeliveryDto(deliveryService.getDeliveryByOrderRefId(orderRefId));
        log.debug("GET /delivery/order/ - deliveryDto: {}", deliveryDto);

        return ResponseEntity.ok(deliveryDto);
    }

    @PostMapping
    @Override
    public ResponseEntity<DeliveryDto> create(@RequestBody CreateDeliveryRequest createDeliveryRequest) {
        log.info("POST delivery - orderRefId: {}, paymentRefId: {}, street: {}, city: {}, postalCode: {}",
            createDeliveryRequest.getOrderRefId(),
            createDeliveryRequest.getPaymentRefId(),
            createDeliveryRequest.getStreet(),
            createDeliveryRequest.getCity(),
            createDeliveryRequest.getPostalCode());
        final DeliveryDto newDeliveryDto = deliveryMapper.toDeliveryDto(deliveryService.createDelivery(createDeliveryRequest));
        log.debug("POST delivery - created: {}", newDeliveryDto);

        return ResponseEntity.ok(newDeliveryDto);
    }

    @PutMapping("/{deliveryId}")
    @Override
    public ResponseEntity<DeliveryDto> update(@PathVariable String deliveryId, @RequestBody DeliveryDto deliveryDto) {
        log.info("PUT delivery/{}, deliveryDto {}", deliveryId, deliveryDto);
        final DeliveryDto updatedDelivery = deliveryMapper.toDeliveryDto(deliveryService.updateDelivery(deliveryId, deliveryDto));
        log.debug("PUT delivery - {}", updatedDelivery);

        return ResponseEntity.ok(updatedDelivery);
    }

    @DeleteMapping("/{deliveryId}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable String deliveryId) {
        log.info("DELETE delivery/{}", deliveryId);
        deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.noContent().build();
    }
}
