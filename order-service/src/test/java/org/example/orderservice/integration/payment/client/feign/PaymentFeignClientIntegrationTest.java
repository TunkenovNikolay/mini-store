package org.example.orderservice.integration.payment.client.feign;

import org.example.orderservice.integration.payment.client.PaymentClient;
import org.example.orderservice.integration.payment.dto.PaymentRequest;
import org.example.orderservice.integration.payment.dto.PaymentResponse;
import org.example.orderservice.integration.payment.dto.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.math.BigDecimal;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@EnableWireMock(
    @ConfigureWireMock(
        name = "payment-service",
        port = 9990,
        baseUrlProperties = "http://localhost",
        filesUnderClasspath = "wiremock"
    )
)
class PaymentFeignClientIntegrationTest {

    @Autowired
    private PaymentClient paymentClient;

    @Test
    void testCreatePayment_Status_Processing() {
        PaymentRequest request = PaymentRequest.builder()
            .inquiryRefId(UUID.fromString("3a0f787d-4afe-4fd7-b1d6-a91728a239d1"))
            .amount(BigDecimal.valueOf(100L))
            .currency("RUB")
            .build();

        PaymentResponse response = paymentClient.createPayment(request);

        assertEquals(PaymentStatus.PROCESSING, response.getStatus());
        verify(postRequestedFor(urlEqualTo("/api/v1/payments")));
    }

}