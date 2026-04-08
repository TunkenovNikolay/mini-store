package org.example.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.controller.docs.PaymentControllerDoc;
import org.example.paymentservice.domain.dto.CreatePaymentRequest;
import org.example.paymentservice.domain.dto.PaymentDto;
import org.example.paymentservice.mapper.PaymentMapper;
import org.example.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerDoc {
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @GetMapping("/{paymentId}")
    @Override
    public ResponseEntity<PaymentDto> get(@PathVariable String paymentId) {
        log.info("GET payments/{}", paymentId);
        final PaymentDto paymentDto = paymentMapper.toCreatePaymentDto(paymentService.getPayment(paymentId));
        log.debug("GET payments/{}", paymentId);

        return ResponseEntity.ok(paymentDto);
    }

    @PostMapping
    @Override
    public ResponseEntity<PaymentDto> create(@RequestBody CreatePaymentRequest createPaymentRequest) {
        log.info("POST payments - orderId: {}, customerId: {}, amount: {}, currency: {}",
            createPaymentRequest.getOrderId(),
            createPaymentRequest.getCustomerId(),
            createPaymentRequest.getAmount(),
            createPaymentRequest.getCurrency());
        final PaymentDto newPaymentDto = paymentMapper.toCreatePaymentDto(paymentService.createPayment(createPaymentRequest));
        log.debug("POST payments - created: {}", newPaymentDto);

        return ResponseEntity.ok(newPaymentDto);
    }

    @PutMapping("/{paymentId}")
    @Override
    public ResponseEntity<PaymentDto> update(@PathVariable String paymentId, @RequestBody PaymentDto paymentDto) {
        log.info("PUT payments/{}", paymentId);
        final PaymentDto updatedPayment = paymentMapper.toCreatePaymentDto(paymentService.updatePayment(paymentId, paymentDto));
        log.debug("PUT payments/{}", updatedPayment);

        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{paymentId}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable String paymentId) {
        log.info("DELETE payments/{}", paymentId);
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

}
