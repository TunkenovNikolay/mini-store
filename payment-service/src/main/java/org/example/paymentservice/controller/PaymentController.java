package org.example.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.paymentservice.domain.dto.CreatePaymentRequest;
import org.example.paymentservice.domain.dto.PaymentDto;
import org.example.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> get(@PathVariable String paymentId) {
        log.info("GET payments/{}", paymentId);
        final PaymentDto paymentDto = paymentService.getPayment(paymentId);
        log.debug("GET payments/{}", paymentId);

        return ResponseEntity.ok(paymentDto);
    }

    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody CreatePaymentRequest createPaymentRequest) {
        log.info("POST payments - inquiryRefId: {}, amount: {}, currency: {}",
            createPaymentRequest.getInquiryRefId(), createPaymentRequest.getAmount(),
            createPaymentRequest.getCurrency());
        final PaymentDto newPaymentDto = paymentService.createPayment(createPaymentRequest);
        log.debug("POST payments - created: {}", newPaymentDto);

        return ResponseEntity.ok(newPaymentDto);
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> update(@PathVariable String paymentId, @RequestBody PaymentDto paymentDto) {
        log.info("PUT payments/{}", paymentId);
        final PaymentDto updatedPayment = paymentService.updatePayment(paymentId, paymentDto);
        log.debug("PUT payments/{}", updatedPayment);

        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> delete(@PathVariable String paymentId) {
        log.info("DELETE payments/{}", paymentId);
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

}
