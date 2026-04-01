package org.example.orderservice.integration.payment.client;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.exception.ServiceException;
import org.example.orderservice.integration.payment.client.feign.PaymentFeignClient;
import org.example.orderservice.integration.payment.dto.PaymentRequest;
import org.example.orderservice.integration.payment.dto.PaymentResponse;
import org.example.orderservice.integration.payment.dto.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentClient {

    private final PaymentFeignClient paymentFeignClient;

    @Retry(name = "PaymentRetry")
    @CircuitBreaker(name = "PaymentCircuitBreaker", fallbackMethod = "fallBackPayment")
    @Bulkhead(name = "PaymentBulkhead")
    @RateLimiter(name = "PaymentRateLimiter")
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        try {
            return paymentFeignClient.createPayment(UUID.randomUUID().toString(), paymentRequest);
        } catch (ServiceException ex) {
            log.error("Payment error: status={}, message={}", ex.getStatus(), ex.getMessage());

            // ErrorDecoder уже пометил 4xx через PAYMENT_CLIENT_ERROR (BAD_REQUEST)
            if (ex.getStatus().is4xxClientError()) {
                return PaymentResponse.builder()
                    .paymentId("failed-" + System.currentTimeMillis())
                    .status(PaymentStatus.FAILED)
                    .build();
            }
            // 5xx и другие пробрасываем дальше (PAYMENT_SERVER_ERROR = SERVICE_UNAVAILABLE)
            throw ex;
        }
    }

    public PaymentResponse fallBackPayment(PaymentRequest req, Throwable t) {
        log.warn("Payment CircuitBreaker fallback activated for inquiryRefId: {}", req.getInquiryRefId(), t);
        return PaymentResponse.builder()
            .paymentId("fallback-" + req.getInquiryRefId())
            .status(PaymentStatus.FAILED)
            .build();
    }
}

