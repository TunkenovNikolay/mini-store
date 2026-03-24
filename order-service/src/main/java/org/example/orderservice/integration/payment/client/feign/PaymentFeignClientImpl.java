package org.example.orderservice.integration.payment.client.feign;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.exception.ServiceException;
import org.example.orderservice.integration.payment.client.feign.dto.CreatePaymentRequest;
import org.example.orderservice.integration.payment.client.feign.dto.PaymentDto;
import org.example.orderservice.integration.payment.client.feign.dto.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentFeignClientImpl {

    private final PaymentFeignClient paymentFeignClient;

    public PaymentDto createPayment(CreatePaymentRequest createPaymentRequest) {
        try {
            return paymentFeignClient.createPayment(UUID.randomUUID().toString(), createPaymentRequest);
        } catch (ServiceException ex) {
            log.error("Payment error: status={}, message={}", ex.getStatus(), ex.getMessage());

            // ErrorDecoder уже пометил 4xx через PAYMENT_CLIENT_ERROR (BAD_REQUEST)
            if (ex.getStatus().is4xxClientError()) {
                return PaymentDto.builder()
                    .paymentId("failed-" + System.currentTimeMillis())
                    .status(PaymentStatus.FAILED)
                    .build();
            }
            // 5xx и другие пробрасываем дальше (PAYMENT_SERVER_ERROR = SERVICE_UNAVAILABLE)
            throw ex;
        }
    }
}

