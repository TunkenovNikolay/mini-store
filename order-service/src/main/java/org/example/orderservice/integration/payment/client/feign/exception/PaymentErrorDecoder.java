package org.example.orderservice.integration.payment.client.feign.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.exception.ErrorMessage;
import org.example.orderservice.exception.ServiceException;

@Slf4j
public class PaymentErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() < 500) {
            log.warn("Payment 4xx error: method={}, status={}",
                methodKey, response.status());

            return new ServiceException(
                ErrorMessage.PAYMENT_CLIENT_ERROR,
                String.valueOf(response.status())
            );
        }
        return defaultDecoder.decode(methodKey, response);
    }
}


