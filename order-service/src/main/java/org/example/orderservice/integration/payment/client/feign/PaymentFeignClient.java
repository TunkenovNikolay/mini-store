package org.example.orderservice.integration.payment.client.feign;

import org.example.orderservice.integration.payment.client.feign.dto.CreatePaymentRequest;
import org.example.orderservice.integration.payment.client.feign.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service", url = "http://localhost:8080")
public interface PaymentFeignClient {

    @PostMapping("/api/v1/payments")
    PaymentDto createPayment(@RequestHeader("X-Idempotency-Key") String idempotencyKey,
                             @RequestBody CreatePaymentRequest createPaymentRequest);

}
