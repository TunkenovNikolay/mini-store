package org.example.orderservice.integration.payment.client.feign;

import org.example.orderservice.integration.payment.dto.PaymentRequest;
import org.example.orderservice.integration.payment.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service", url = "${integration.payment-service.base-url}")
public interface PaymentFeignClient {

    @PostMapping()
    PaymentResponse createPayment(@RequestHeader("X-Idempotency-Key") String idempotencyKey,
                                  @RequestBody PaymentRequest paymentRequest);

}
