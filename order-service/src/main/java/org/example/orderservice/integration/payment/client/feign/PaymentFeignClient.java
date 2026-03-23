package org.example.orderservice.integration.payment.client.feign;

import org.example.orderservice.integration.payment.client.feign.dto.CreatePaymentRequest;
import org.example.orderservice.integration.payment.client.feign.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "http://localhost:8080")
public interface PaymentFeignClient {

    @PostMapping("/api/v1/payments")
    PaymentDto createPayment(@RequestBody CreatePaymentRequest createPaymentRequest);

}
